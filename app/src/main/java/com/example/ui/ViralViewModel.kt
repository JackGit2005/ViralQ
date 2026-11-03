package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.ViralAnalyzerService
import com.example.data.PresetVideo
import com.example.data.PresetVideos
import com.example.data.local.AppDatabase
import com.example.data.local.ClipEntity
import com.example.data.local.ExportJobEntity
import com.example.model.AspectRatioOption
import com.example.model.CaptionPosition
import com.example.model.CaptionStyleType
import com.example.model.FramingMode
import com.example.model.VideoAnalysisResult
import com.example.model.ViralMomentClip
import com.example.util.YouTubeParser
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.UUID

sealed interface UiState {
    data object Idle : UiState
    data class Analyzing(val stepMessage: String, val progress: Float) : UiState
    data class Success(val result: VideoAnalysisResult) : UiState
    data class Error(val errorMessage: String) : UiState
}

class ViralViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val clipDao = db.clipDao()
    private val exportDao = db.exportJobDao()
    private val analyzerService = ViralAnalyzerService()

    // Navigation Tab: 0 = Home, 1 = Studio, 2 = Saved
    val currentTab = MutableStateFlow(1)

    // Form inputs
    val inputUrl = MutableStateFlow("https://www.youtube.com/watch?v=kffacxfA7G4")
    val selectedFormat = MutableStateFlow(AspectRatioOption.RATIO_9_16)
    val selectedCategory = MutableStateFlow("All Viral Moments")
    val promptCoPilotQuery = MutableStateFlow("")

    private val initialAnalysis = analyzerService.generateRichFallback(
        url = "https://www.youtube.com/watch?v=kffacxfA7G4",
        videoId = "kffacxfA7G4",
        format = AspectRatioOption.RATIO_9_16,
        preset = PresetVideos.list.firstOrNull()
    )

    private val _uiState = MutableStateFlow<UiState>(UiState.Success(initialAnalysis))
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    // Studio Player & Editing State
    val selectedClipIndex = MutableStateFlow(0)
    val selectedCaptionStyle = MutableStateFlow(CaptionStyleType.PUNCHY_POP)
    val captionPosition = MutableStateFlow(CaptionPosition.BOTTOM)
    val framingMode = MutableStateFlow(FramingMode.SMART_SPEAKER_TRACK)
    val captionFontSize = MutableStateFlow(20f)
    val showSafeZones = MutableStateFlow(true)
    val isPlaying = MutableStateFlow(true)
    val currentPlayTimeSeconds = MutableStateFlow(0f)

    // Feedback Toast
    val toastMessage = MutableStateFlow<String?>(null)

    // Search and Filter for Saved Clips
    val searchQuery = MutableStateFlow("")
    val savedFilterMode = MutableStateFlow("ALL") // "ALL", "FAVORITES", "TOP_SCORE"

    // Saved Clips list from Room
    val savedClips: StateFlow<List<ClipEntity>> = combine(
        searchQuery,
        savedFilterMode
    ) { query, filter -> Pair(query, filter) }
        .flatMapLatest { (query, filter) ->
            when {
                query.isNotBlank() -> clipDao.searchClips(query.trim())
                filter == "FAVORITES" -> clipDao.getFavoriteClips()
                filter == "TOP_SCORE" -> clipDao.getClipsSortedByScore()
                else -> clipDao.getAllSavedClips()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Export Job History from Room
    val exportHistory: StateFlow<List<ExportJobEntity>> = exportDao.getAllExports()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Active Render Export Pipeline
    val isExporting = MutableStateFlow(false)
    val exportProgress = MutableStateFlow(0f)
    val exportStepMessage = MutableStateFlow("")
    private var exportJob: Job? = null

    // Playback loop job
    private var playbackJob: Job? = null

    init {
        startPlaybackLoop()
    }

    fun setTab(index: Int) {
        currentTab.value = index.coerceIn(0, 2)
    }

    fun setInputUrl(url: String) {
        inputUrl.value = url
    }

    fun selectPreset(preset: PresetVideo) {
        inputUrl.value = preset.url
        analyzeCurrentUrl()
    }

    fun setFormat(format: AspectRatioOption) {
        selectedFormat.value = format
    }

    fun setCategory(category: String) {
        selectedCategory.value = category
    }

    fun selectClip(index: Int) {
        selectedClipIndex.value = index
        currentPlayTimeSeconds.value = 0f
    }

    fun setCaptionStyle(style: CaptionStyleType) {
        selectedCaptionStyle.value = style
    }

    fun setCaptionPosition(position: CaptionPosition) {
        captionPosition.value = position
    }

    fun setFramingMode(mode: FramingMode) {
        framingMode.value = mode
    }

    fun setPromptCoPilot(query: String) {
        promptCoPilotQuery.value = query
    }

    fun togglePlayPause() {
        isPlaying.value = !isPlaying.value
    }

    fun toggleSafeZones() {
        showSafeZones.value = !showSafeZones.value
    }

    fun seekTo(seconds: Float) {
        currentPlayTimeSeconds.value = seconds
    }

    fun clearToast() {
        toastMessage.value = null
    }

    fun showToast(msg: String) {
        toastMessage.value = msg
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setFilterMode(mode: String) {
        savedFilterMode.value = mode
    }

    // -----------------------------------------------------------------------
    // Precision Clip Boundary Trimming (+/- 0.5s Nudge with Validation)
    // -----------------------------------------------------------------------
    fun nudgeClipStart(deltaSeconds: Float) {
        val currentSuccess = _uiState.value as? UiState.Success ?: return
        val clips = currentSuccess.result.clips.toMutableList()
        val index = selectedClipIndex.value.coerceIn(0, clips.lastIndex)
        val clip = clips[index]

        val newStart = (clip.startSeconds + deltaSeconds.toInt()).coerceAtLeast(0)
        val newDuration = clip.endSeconds - newStart

        if (newDuration < 5) {
            showToast("Clip minimum length is 5 seconds")
            return
        }
        if (newStart >= clip.endSeconds) {
            showToast("Start time cannot exceed end time")
            return
        }

        val updatedClip = clip.copy(
            startSeconds = newStart,
            timeRangeFormatted = YouTubeParser.formatSecondsToTime(newStart) + " - " + YouTubeParser.formatSecondsToTime(clip.endSeconds)
        )
        clips[index] = updatedClip
        _uiState.value = UiState.Success(currentSuccess.result.copy(clips = clips))
        currentPlayTimeSeconds.value = 0f
        showToast("Start adjusted to ${YouTubeParser.formatSecondsToTime(newStart)} (${newDuration}s)")
    }

    fun nudgeClipEnd(deltaSeconds: Float) {
        val currentSuccess = _uiState.value as? UiState.Success ?: return
        val clips = currentSuccess.result.clips.toMutableList()
        val index = selectedClipIndex.value.coerceIn(0, clips.lastIndex)
        val clip = clips[index]

        val newEnd = (clip.endSeconds + deltaSeconds.toInt()).coerceAtLeast(clip.startSeconds + 5)
        val newDuration = newEnd - clip.startSeconds

        if (newDuration > 120) {
            showToast("Clip maximum length is 120 seconds for Shorts/Reels")
            return
        }

        val updatedClip = clip.copy(
            endSeconds = newEnd,
            timeRangeFormatted = YouTubeParser.formatSecondsToTime(clip.startSeconds) + " - " + YouTubeParser.formatSecondsToTime(newEnd)
        )
        clips[index] = updatedClip
        _uiState.value = UiState.Success(currentSuccess.result.copy(clips = clips))
        showToast("End adjusted to ${YouTubeParser.formatSecondsToTime(newEnd)} (${newDuration}s)")
    }

    fun updateClipBounds(newStart: Int, newEnd: Int) {
        val currentSuccess = _uiState.value as? UiState.Success ?: return
        val clips = currentSuccess.result.clips.toMutableList()
        val index = selectedClipIndex.value.coerceIn(0, clips.lastIndex)
        val clip = clips[index]

        val safeStart = newStart.coerceAtLeast(0)
        val safeEnd = newEnd.coerceAtLeast(safeStart + 5)
        val duration = safeEnd - safeStart

        if (duration < 5) return
        if (duration > 180) return

        val updatedClip = clip.copy(
            startSeconds = safeStart,
            endSeconds = safeEnd,
            durationSeconds = duration,
            timeRangeFormatted = YouTubeParser.formatSecondsToTime(safeStart) + " - " + YouTubeParser.formatSecondsToTime(safeEnd)
        )
        clips[index] = updatedClip
        _uiState.value = UiState.Success(currentSuccess.result.copy(clips = clips))
    }

    // -----------------------------------------------------------------------
    // AI Video Analysis Flow
    // -----------------------------------------------------------------------
    fun analyzeCurrentUrl() {
        val url = inputUrl.value.trim()
        if (url.isBlank()) {
            _uiState.value = UiState.Error("Please enter or paste a valid YouTube video URL.")
            return
        }

        viewModelScope.launch {
            _uiState.value = UiState.Analyzing("Extracting stream markers & video metadata...", 0.15f)
            delay(400)
            _uiState.value = UiState.Analyzing("Scanning audio spikes & emotional peaks...", 0.45f)
            delay(400)
            _uiState.value = UiState.Analyzing("Gemini 3.8 Flash evaluating viral retention...", 0.75f)

            val result = analyzerService.analyzeVideo(
                youtubeUrl = url,
                selectedFormat = selectedFormat.value,
                userCategoryPreference = selectedCategory.value,
                promptCoPilotQuery = promptCoPilotQuery.value
            )

            result.fold(
                onSuccess = { analysis ->
                    _uiState.value = UiState.Success(analysis)
                    selectedClipIndex.value = 0
                    currentPlayTimeSeconds.value = 0f
                    isPlaying.value = true
                    currentTab.value = 1 // Switch to Studio
                    showToast("Found ${analysis.clips.size} viral moments!")
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error.localizedMessage ?: "Failed to analyze video.")
                }
            )
        }
    }

    private fun startPlaybackLoop() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            while (true) {
                delay(100)
                if (isPlaying.value) {
                    val currentSuccess = _uiState.value as? UiState.Success
                    if (currentSuccess != null && currentSuccess.result.clips.isNotEmpty()) {
                        val activeClip = currentSuccess.result.clips.getOrNull(selectedClipIndex.value)
                        val maxDuration = (activeClip?.durationSeconds ?: 30).toFloat()
                        val newTime = currentPlayTimeSeconds.value + 0.1f
                        if (newTime >= maxDuration) {
                            currentPlayTimeSeconds.value = 0f
                        } else {
                            currentPlayTimeSeconds.value = newTime
                        }
                    }
                }
            }
        }
    }

    // -----------------------------------------------------------------------
    // MP4 Video Render Pipeline Simulation & Room Storage
    // -----------------------------------------------------------------------
    fun startRenderExport(resolution: String, burnedInCaptions: Boolean) {
        val currentSuccess = _uiState.value as? UiState.Success ?: return
        val activeClip = currentSuccess.result.clips.getOrNull(selectedClipIndex.value) ?: return

        exportJob?.cancel()
        exportJob = viewModelScope.launch {
            isExporting.value = true
            exportProgress.value = 0.05f
            exportStepMessage.value = "Initializing hardware encoder & surface..."
            delay(400)

            exportProgress.value = 0.25f
            exportStepMessage.value = "Extracting video segment (${activeClip.timeRangeFormatted})..."
            delay(500)

            exportProgress.value = 0.55f
            exportStepMessage.value = if (burnedInCaptions) {
                "Burning in ${selectedCaptionStyle.value.displayName} captions..."
            } else {
                "Remuxing audio & video streams (${resolution})..."
            }
            delay(600)

            exportProgress.value = 0.85f
            exportStepMessage.value = "Finalizing MP4 container & color grading..."
            delay(400)

            exportProgress.value = 1.0f
            exportStepMessage.value = "Export complete!"
            delay(200)

            val fileName = "Virallo_${activeClip.id.take(6)}_${selectedFormat.value.label.replace(":", "x")}.mp4"
            val fileSizeBytes = (activeClip.durationSeconds * 1_250_000L).coerceAtLeast(5_000_000L)

            val jobEntity = ExportJobEntity(
                id = UUID.randomUUID().toString(),
                clipId = activeClip.id,
                clipTitle = activeClip.title,
                resolution = resolution,
                aspectRatio = selectedFormat.value.label,
                burnedInCaptions = burnedInCaptions,
                captionStyle = selectedCaptionStyle.value.displayName,
                status = "COMPLETED",
                outputFileName = fileName,
                fileSizeBytes = fileSizeBytes,
                durationSeconds = activeClip.durationSeconds
            )
            exportDao.insertExport(jobEntity)

            isExporting.value = false
            showToast("Exported $fileName (${resolution}) successfully!")
        }
    }

    fun cancelExport() {
        exportJob?.cancel()
        isExporting.value = false
        exportProgress.value = 0f
        showToast("Export cancelled.")
    }

    // -----------------------------------------------------------------------
    // Bookmark & Favorites Management
    // -----------------------------------------------------------------------
    fun toggleBookmarkCurrentClip() {
        val currentSuccess = _uiState.value as? UiState.Success ?: return
        val clip = currentSuccess.result.clips.getOrNull(selectedClipIndex.value) ?: return

        viewModelScope.launch {
            val isSaved = clipDao.isClipSaved(clip.id)
            if (isSaved) {
                clipDao.deleteClipById(clip.id)
                showToast("Removed from saved library.")
            } else {
                val entity = ClipEntity(
                    id = clip.id,
                    videoId = currentSuccess.result.videoId,
                    videoTitle = currentSuccess.result.title,
                    channel = currentSuccess.result.channel,
                    thumbnailUrl = currentSuccess.result.thumbnailUrl,
                    clipTitle = clip.title,
                    startSeconds = clip.startSeconds,
                    endSeconds = clip.endSeconds,
                    timeRangeFormatted = clip.timeRangeFormatted,
                    overallScore = clip.metrics.overallScore,
                    hookScore = clip.metrics.hookScore,
                    retentionScore = clip.metrics.retentionScore,
                    emotionalScore = clip.metrics.emotionalIntensity,
                    shareabilityScore = clip.metrics.shareabilityScore,
                    whyViralExplanation = clip.whyViralExplanation,
                    creatorParallels = clip.creatorParallels,
                    bestPlatform = clip.bestPlatform,
                    formatRatio = selectedFormat.value.label,
                    tagsJson = JSONArray(clip.tags).toString(),
                    captionsJson = clip.captions.joinToString(";") { "${it.startSeconds}:${it.endSeconds}:${it.text}:${it.highlightedWord}" },
                    captionOptionsJson = clip.captionOptions.joinToString(";") { "${it.category}|${it.captionText}|${it.impactAnalysis}|${it.designTip}" },
                    platformLinksJson = clip.platformLinks.joinToString(";") { "${it.platformName}|${it.url}|${it.label}" },
                    isFavorite = false
                )
                clipDao.insertClip(entity)
                showToast("Saved clip to your library!")
            }
        }
    }

    fun toggleFavorite(clipId: String, currentFavorite: Boolean) {
        viewModelScope.launch {
            clipDao.updateFavorite(clipId, !currentFavorite)
            showToast(if (!currentFavorite) "Marked as favorite!" else "Removed from favorites.")
        }
    }

    fun removeSavedClip(clipId: String) {
        viewModelScope.launch {
            clipDao.deleteClipById(clipId)
            showToast("Clip removed from library.")
        }
    }
}
