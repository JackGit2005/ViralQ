package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.ClipEntity
import com.example.data.local.ExportJobEntity
import com.example.model.AspectRatioOption
import com.example.model.FramingMode
import com.example.model.ViralMomentClip
import com.example.ui.UiState
import com.example.ui.ViralViewModel
import com.example.ui.components.CaptionStudioCard
import com.example.ui.components.HookAndSchedulerCard
import com.example.ui.components.PlatformLinksCard
import com.example.ui.components.RetentionEngineCard
import com.example.ui.components.VideoPlayerPreview
import com.example.ui.components.ViralScoreCard
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioBorderSubtle
import com.example.ui.theme.StudioCaptionRed
import com.example.ui.theme.StudioCaptionYellow
import com.example.ui.theme.StudioDarkBg
import com.example.ui.theme.StudioGreen
import com.example.ui.theme.StudioPrimaryViolet
import com.example.ui.theme.StudioPrimaryViolet
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.StudioSurfaceSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.YouTubeParser

/**
 * Virallo Creator Studio — Root Screen.
 * Redesigned for cinematic elegance, clarity, and mobile-first ergonomics.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: ViralViewModel,
    modifier: Modifier = Modifier
) {
    val currentTab by viewModel.currentTab.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    val inputUrl by viewModel.inputUrl.collectAsState()
    val selectedFormat by viewModel.selectedFormat.collectAsState()
    val promptCoPilotQuery by viewModel.promptCoPilotQuery.collectAsState()
    val framingMode by viewModel.framingMode.collectAsState()
    val selectedClipIndex by viewModel.selectedClipIndex.collectAsState()
    val selectedCaptionStyle by viewModel.selectedCaptionStyle.collectAsState()
    val captionPosition by viewModel.captionPosition.collectAsState()
    val captionFontSize by viewModel.captionFontSize.collectAsState()
    val showSafeZones by viewModel.showSafeZones.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPlayTime by viewModel.currentPlayTimeSeconds.collectAsState()
    val savedClips by viewModel.savedClips.collectAsState()
    val exportHistory by viewModel.exportHistory.collectAsState()
    val isExporting by viewModel.isExporting.collectAsState()
    val exportProgress by viewModel.exportProgress.collectAsState()
    val exportStepMessage by viewModel.exportStepMessage.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    var currentDetailSegment by remember { mutableIntStateOf(0) }
    var showAboutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = StudioDarkBg,
        contentWindowInsets = WindowInsets.navigationBars,
        topBar = {
            ViralloTopBar(
                savedCount = savedClips.size,
                currentTab = currentTab,
                onNewLinkClick = { viewModel.setTab(0) },
                onInfoClick = { showAboutDialog = true }
            )
        },
        bottomBar = {
            ViralloBottomNavigation(
                currentTab = currentTab,
                savedCount = savedClips.size,
                onSelectTab = { viewModel.setTab(it) }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(StudioDarkBg)
        ) {
            AnimatedContent(
                targetState = currentTab,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally(
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            initialOffsetX = { it / 4 }
                        ) + fadeIn()).togetherWith(
                            slideOutHorizontally(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                targetOffsetX = { -it / 4 }
                            ) + fadeOut()
                        )
                    } else {
                        (slideInHorizontally(
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                            initialOffsetX = { -it / 4 }
                        ) + fadeIn()).togetherWith(
                            slideOutHorizontally(
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                                targetOffsetX = { it / 4 }
                            ) + fadeOut()
                        )
                    }
                },
                modifier = Modifier.fillMaxSize(),
                label = "main_tabs_animated_content"
            ) { activeTab ->
                when (activeTab) {
                    0 -> {
                        // TAB 0: Video Input & Discovery
                        VideoInputView(
                            inputUrl = inputUrl,
                            selectedFormat = selectedFormat,
                            promptCoPilotQuery = promptCoPilotQuery,
                            framingMode = framingMode,
                            isAnalyzing = uiState is UiState.Analyzing,
                            onUrlChange = { viewModel.setInputUrl(it) },
                            onPasteUrl = {
                                clipboardManager.getText()?.text?.let {
                                    viewModel.setInputUrl(it)
                                    viewModel.showToast("Pasted URL from clipboard")
                                }
                            },
                            onClearUrl = { viewModel.setInputUrl("") },
                            onFormatChange = { viewModel.setFormat(it) },
                            onCoPilotChange = { viewModel.setPromptCoPilot(it) },
                            onFramingModeChange = { viewModel.setFramingMode(it) },
                            onSelectPreset = { preset ->
                                viewModel.selectPreset(preset)
                            },
                            onAnalyze = {
                                viewModel.analyzeCurrentUrl()
                            }
                        )
                    }

                    1 -> {
                        // TAB 1: Studio & Candidate Clips
                        when (val state = uiState) {
                            is UiState.Idle -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Surface(
                                        color = StudioSurface,
                                        shape = RoundedCornerShape(20.dp),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .widthIn(max = 480.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(28.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Surface(
                                                color = StudioPrimaryViolet.copy(alpha = 0.2f),
                                                shape = CircleShape,
                                                modifier = Modifier.size(64.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.Center) {
                                                    Icon(
                                                        imageVector = Icons.Default.Movie,
                                                        contentDescription = null,
                                                        tint = StudioPrimaryViolet,
                                                        modifier = Modifier.size(32.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(18.dp))
                                            Text(
                                                text = "No Video Loaded",
                                                color = TextPrimary,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Paste a YouTube link in Home or select a trending preset to discover viral moments.",
                                                color = TextSecondary,
                                                fontSize = 13.sp,
                                                textAlign = TextAlign.Center,
                                                lineHeight = 18.sp
                                            )
                                            Spacer(modifier = Modifier.height(22.dp))
                                            Button(
                                                onClick = { viewModel.setTab(0) },
                                                colors = ButtonDefaults.buttonColors(containerColor = StudioPrimaryViolet),
                                                shape = RoundedCornerShape(12.dp),
                                                modifier = Modifier.height(44.dp)
                                            ) {
                                                Text("Paste YouTube Link", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            }
                                        }
                                    }
                                }
                            }

                            is UiState.Analyzing -> {
                                AnalyzingChamber(
                                    stepMessage = state.stepMessage,
                                    progress = state.progress
                                )
                            }

                            is UiState.Error -> {
                                ErrorChamber(
                                    message = state.errorMessage,
                                    onRetry = { viewModel.analyzeCurrentUrl() },
                                    onOpenInput = { viewModel.setTab(0) }
                                )
                            }

                            is UiState.Success -> {
                                val result = state.result
                                val activeClip = result.clips.getOrNull(selectedClipIndex)
                                    ?: result.clips.firstOrNull()

                                if (activeClip != null) {
                                    val isBookmarked = savedClips.any { it.id == activeClip.id }

                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState())
                                            .padding(horizontal = 16.dp, vertical = 10.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(14.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .widthIn(max = 640.dp)
                                        ) {
                                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                                // 1. Sleek Horizontal Clip Selection Cards
                                                ClipSelectionCardsRow(
                                                    clips = result.clips,
                                                    selectedIndex = selectedClipIndex,
                                                    onSelect = { viewModel.selectClip(it) }
                                                )

                                                // 2. Video Player Preview & Precision Timeline
                                                VideoPlayerPreview(
                                                    clip = activeClip,
                                                    videoId = result.videoId,
                                                    thumbnailUrl = result.thumbnailUrl,
                                                    aspectRatio = selectedFormat,
                                                    captionStyle = selectedCaptionStyle,
                                                    captionPosition = captionPosition,
                                                    framingMode = framingMode,
                                                    fontSizeSp = captionFontSize,
                                                    isPlaying = isPlaying,
                                                    currentTimeSeconds = currentPlayTime,
                                                    showSafeZones = showSafeZones,
                                                    onFormatChange = { viewModel.setFormat(it) },
                                                    onTogglePlay = { viewModel.togglePlayPause() },
                                                    onSeek = { viewModel.seekTo(it) },
                                                    onToggleSafeZones = { viewModel.toggleSafeZones() },
                                                    onBookmarkToggle = { viewModel.toggleBookmarkCurrentClip() },
                                                    isBookmarked = isBookmarked,
                                                    onNudgeStart = { viewModel.nudgeClipStart(it) },
                                                    onNudgeEnd = { viewModel.nudgeClipEnd(it) },
                                                    onUpdateBounds = { start, end -> viewModel.updateClipBounds(start, end) }
                                                )

                                                // 3. Active Clip Metadata Strip with Quick Bookmark
                                                Surface(
                                                    color = StudioSurface,
                                                    shape = RoundedCornerShape(14.dp),
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 14.dp, vertical = 10.dp),
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween
                                                    ) {
                                                        Column(modifier = Modifier.weight(1f)) {
                                                            Text(
                                                                text = activeClip.title,
                                                                color = TextPrimary,
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 13.sp,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                            Spacer(modifier = Modifier.height(2.dp))
                                                            Text(
                                                                text = "${result.title} • ${activeClip.timeRangeFormatted} (${activeClip.durationSeconds}s)",
                                                                color = TextMuted,
                                                                fontSize = 11.sp,
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        }

                                                        Spacer(modifier = Modifier.width(8.dp))

                                                        IconButton(
                                                            onClick = { viewModel.toggleBookmarkCurrentClip() },
                                                            modifier = Modifier
                                                                .size(34.dp)
                                                                .background(StudioSurfaceElevated, CircleShape)
                                                                .testTag("bookmark_clip_button")
                                                        ) {
                                                            Icon(
                                                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                                                contentDescription = "Bookmark",
                                                                tint = if (isBookmarked) StudioCaptionYellow else TextSecondary,
                                                                modifier = Modifier.size(17.dp)
                                                            )
                                                        }
                                                    }
                                                }

                                                // 4. Studio Editing Tool Toolbar (Clean Modular Tabs)
                                                ViralloEditingToolbar(
                                                    selectedSegment = currentDetailSegment,
                                                    onSelect = { currentDetailSegment = it }
                                                )

                                                // 5. Active Tool Content Drawer
                                                AnimatedContent(
                                                    targetState = currentDetailSegment,
                                                    transitionSpec = {
                                                        fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
                                                            .togetherWith(fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMediumLow)))
                                                    },
                                                    label = "editing_tool_drawer_transition"
                                                ) { segment ->
                                                    when (segment) {
                                                        0 -> {
                                                            // Captions Studio
                                                            CaptionStudioCard(
                                                                clip = activeClip,
                                                                selectedStyle = selectedCaptionStyle,
                                                                selectedPosition = captionPosition,
                                                                fontSizeSp = captionFontSize,
                                                                onSelectStyle = { viewModel.setCaptionStyle(it) },
                                                                onSelectPosition = { viewModel.setCaptionPosition(it) },
                                                                onFontSizeChange = { viewModel.captionFontSize.value = it },
                                                                onShowToast = { viewModel.showToast(it) }
                                                            )
                                                        }

                                                        1 -> {
                                                            // Crop & Reframing
                                                            CropAndFramingCard(
                                                                aspectRatio = selectedFormat,
                                                                framingMode = framingMode,
                                                                onFormatChange = { viewModel.setFormat(it) },
                                                                onFramingChange = { viewModel.setFramingMode(it) }
                                                            )
                                                        }

                                                        2 -> {
                                                            // Viral Intel & Retention Engine
                                                            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                                                                ViralScoreCard(clip = activeClip)
                                                                RetentionEngineCard(clip = activeClip)
                                                            }
                                                        }

                                                        3 -> {
                                                            // Hooks & Multi-Platform Scheduler
                                                            HookAndSchedulerCard(
                                                                clip = activeClip,
                                                                onShowToast = { viewModel.showToast(it) }
                                                            )
                                                        }

                                                        4 -> {
                                                            // Export & Platform Hub
                                                            PlatformLinksCard(
                                                                clip = activeClip,
                                                                onShowToast = { viewModel.showToast(it) },
                                                                isExporting = isExporting,
                                                                exportProgress = exportProgress,
                                                                exportStepMessage = exportStepMessage,
                                                                onStartExport = { res, burned ->
                                                                    viewModel.startRenderExport(res, burned)
                                                                },
                                                                onCancelExport = {
                                                                    viewModel.cancelExport()
                                                                }
                                                            )
                                                        }
                                                    }
                                                }

                                                // 6. Action Hierarchy: Primary Export Action & Save Secondary
                                                Surface(
                                                    color = StudioSurface,
                                                    shape = RoundedCornerShape(16.dp),
                                                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                                                    modifier = Modifier.fillMaxWidth()
                                                ) {
                                                    Row(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(12.dp),
                                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        // Primary Action: Export Clip
                                                        Button(
                                                            onClick = { currentDetailSegment = 4 },
                                                            colors = ButtonDefaults.buttonColors(containerColor = StudioPrimaryViolet),
                                                            shape = RoundedCornerShape(12.dp),
                                                            modifier = Modifier
                                                                .weight(1f)
                                                                .height(46.dp)
                                                        ) {
                                                            Icon(
                                                                imageVector = Icons.Default.Download,
                                                                contentDescription = null,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                            Spacer(modifier = Modifier.width(6.dp))
                                                            Text(
                                                                text = "Export Clip",
                                                                fontWeight = FontWeight.Bold,
                                                                fontSize = 13.sp
                                                            )
                                                        }

                                                        // Secondary Action: Copy SRT
                                                        Surface(
                                                            color = StudioSurfaceElevated,
                                                            shape = RoundedCornerShape(12.dp),
                                                            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                                                            modifier = Modifier
                                                                .clip(RoundedCornerShape(12.dp))
                                                                .clickable {
                                                                    val srt = YouTubeParser.generateSrtContent(activeClip.captions)
                                                                    clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(srt))
                                                                    viewModel.showToast("Captions SRT copied to clipboard")
                                                                }
                                                                .height(46.dp)
                                                        ) {
                                                            Row(
                                                                modifier = Modifier.padding(horizontal = 14.dp),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Icon(
                                                                    imageVector = Icons.Default.ContentCopy,
                                                                    contentDescription = "Copy SRT",
                                                                    tint = TextSecondary,
                                                                    modifier = Modifier.size(16.dp)
                                                                )
                                                                Spacer(modifier = Modifier.width(6.dp))
                                                                Text(
                                                                    text = "SRT",
                                                                    color = TextPrimary,
                                                                    fontWeight = FontWeight.SemiBold,
                                                                    fontSize = 12.sp
                                                                )
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // TAB 2: Saved Library & Render History
                        SavedClipsView(
                            savedClips = savedClips,
                            exportJobs = exportHistory,
                            searchQuery = viewModel.searchQuery.collectAsState().value,
                            filterMode = viewModel.savedFilterMode.collectAsState().value,
                            onSearchQueryChange = { viewModel.setSearchQuery(it) },
                            onFilterChange = { viewModel.setFilterMode(it) },
                            onToggleFavorite = { id, fav -> viewModel.toggleFavorite(id, fav) },
                            onRemove = { viewModel.removeSavedClip(it) },
                            onShowToast = { viewModel.showToast(it) },
                            onGoToInput = { viewModel.setTab(0) }
                        )
                    }
                }
            }
        }
    }

    // About & Viral Formula Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(StudioPrimaryViolet),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("About Virallo Studio", color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Virallo is a production-minded creator studio that identifies high-retention short-form clips, reframes footage, generates dynamic animated subtitles, and prepares platform-ready assets.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    Surface(
                        color = StudioSurfaceElevated,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "⚡ AI Model Engine:", color = StudioPrimaryViolet, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(text = "Gemini 3.8 Flash (via Firebase AI Logic server-side)", color = TextPrimary, fontSize = 11.sp)
                        }
                    }

                    Surface(
                        color = StudioSurfaceElevated,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "📊 Transparent Viral Heuristic Formula:", color = StudioCaptionYellow, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text(
                                text = "• Hook Strength: 30%\n• Emotional Intensity: 25%\n• Narrative Payoff: 20%\n• Standalone Context: 15%\n• Shareability: 10%",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    Text(
                        text = "ℹ️ Editorial Disclaimer: Viral Potential Scores represent editorial evaluations based on engagement heuristics. They are not measured views.",
                        color = TextMuted,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Close", color = StudioPrimaryViolet, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = StudioSurface,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

// ---------------------------------------------------------------------------
// VIRALLO TOP APP BAR
// ---------------------------------------------------------------------------
@Composable
private fun ViralloTopBar(
    savedCount: Int,
    currentTab: Int,
    onNewLinkClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Surface(
        color = StudioSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Brand & Title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(StudioPrimaryViolet),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Virallo",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Virallo",
                            color = TextPrimary,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = StudioPrimaryViolet.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "STUDIO",
                                color = StudioPrimaryViolet,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "AI Viral Clips & Caption Engine",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Quick Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onInfoClick,
                    modifier = Modifier
                        .size(32.dp)
                        .background(StudioSurfaceElevated, CircleShape)
                        .testTag("top_bar_about_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    color = if (currentTab == 0) StudioPrimaryViolet else StudioSurfaceElevated,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onNewLinkClick() }
                        .testTag("top_bar_new_input_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Paste Link",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Paste Link",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// CLIP SELECTION CARDS ROW
// ---------------------------------------------------------------------------
@Composable
private fun ClipSelectionCardsRow(
    clips: List<ViralMomentClip>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "DETECTED HIGHLIGHTS (${clips.size})",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Select a clip to edit & preview",
                color = TextMuted,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 2.dp)
        ) {
            itemsIndexed(clips) { index, clip ->
                val isSelected = index == selectedIndex
                Surface(
                    color = if (isSelected) StudioSurfaceElevated else StudioSurface,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        if (isSelected) 1.5.dp else 1.dp,
                        if (isSelected) StudioPrimaryViolet else StudioBorderSubtle
                    ),
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onSelect(index) }
                        .testTag("clip_carousel_item_$index")
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Clip #${index + 1}",
                                color = if (isSelected) Color.White else TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${clip.durationSeconds}s",
                                color = TextMuted,
                                fontFamily = FontFamily.Monospace,
                                fontSize = 10.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = clip.title,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Score Badge
                        Surface(
                            color = if (clip.metrics.overallScore >= 95) Color(0x33EF4444) else StudioPrimaryViolet.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalFireDepartment,
                                    contentDescription = null,
                                    tint = if (clip.metrics.overallScore >= 95) StudioCaptionRed else StudioPrimaryViolet,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${clip.metrics.overallScore}% VIRAL",
                                    color = if (clip.metrics.overallScore >= 95) StudioCaptionRed else StudioPrimaryViolet,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// VIRALLO EDITING TOOLBAR (Tabbed Drawer Selector)
// ---------------------------------------------------------------------------
@Composable
private fun ViralloEditingToolbar(
    selectedSegment: Int,
    onSelect: (Int) -> Unit
) {
    val items = listOf(
        Triple("Captions", Icons.Default.Subtitles, 0),
        Triple("Framing", Icons.Default.Crop, 1),
        Triple("Intel", Icons.Default.TrendingUp, 2),
        Triple("Hooks", Icons.Default.GraphicEq, 3),
        Triple("Export", Icons.Default.Share, 4)
    )

    Surface(
        color = StudioSurface,
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items.forEach { (label, icon, index) ->
                val isSelected = selectedSegment == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) StudioPrimaryViolet else Color.Transparent)
                        .clickable { onSelect(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) Color.White else TextMuted,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TextMuted,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// CROP & REFRAMING EDITING CARD
// ---------------------------------------------------------------------------
@Composable
private fun CropAndFramingCard(
    aspectRatio: AspectRatioOption,
    framingMode: FramingMode,
    onFormatChange: (AspectRatioOption) -> Unit,
    onFramingChange: (FramingMode) -> Unit
) {
    Surface(
        color = StudioSurface,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Crop,
                    contentDescription = null,
                    tint = StudioPrimaryViolet,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CANVAS & DYNAMIC FRAMING",
                    color = StudioPrimaryViolet,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("Aspect Ratio Target:", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AspectRatioOption.values().forEach { option ->
                    val isSelected = aspectRatio == option
                    Surface(
                        color = if (isSelected) StudioPrimaryViolet.copy(alpha = 0.25f) else StudioSurfaceElevated,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioPrimaryViolet else StudioBorderSubtle
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onFormatChange(option) }
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = option.label,
                                color = if (isSelected) Color.White else TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = option.platformTarget,
                                color = TextMuted,
                                fontSize = 9.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("AI Camera Operator & Subject Framing:", color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(6.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(
                    Pair(FramingMode.SMART_SPEAKER_TRACK, "Smart Speaker Track (Auto-Pan & Zoom)"),
                    Pair(FramingMode.SPLIT_SCREEN_STACKED, "Split-Screen Stacked (Multi-Speaker Podcast)"),
                    Pair(FramingMode.DYNAMIC_MOTION_PAN, "Dynamic Motion Pan (Action Tracker)")
                ).forEach { (mode, label) ->
                    val isSelected = framingMode == mode
                    Surface(
                        color = if (isSelected) StudioSurfaceElevated else StudioSurface,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) StudioPrimaryViolet else StudioBorderSubtle
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onFramingChange(mode) }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(if (isSelected) StudioPrimaryViolet else TextMuted, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = label,
                                color = if (isSelected) Color.White else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// MATERIAL 3 BOTTOM NAVIGATION BAR (Home, Studio, Saved)
// ---------------------------------------------------------------------------
@Composable
private fun ViralloBottomNavigation(
    currentTab: Int,
    savedCount: Int,
    onSelectTab: (Int) -> Unit
) {
    NavigationBar(
        containerColor = StudioSurface,
        tonalElevation = 8.dp,
        modifier = Modifier.border(androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle))
    ) {
        NavigationBarItem(
            selected = currentTab == 0,
            onClick = { onSelectTab(0) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = StudioPrimaryViolet,
                indicatorColor = StudioPrimaryViolet,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("bottom_nav_home")
        )

        NavigationBarItem(
            selected = currentTab == 1,
            onClick = { onSelectTab(1) },
            icon = { Icon(Icons.Default.Movie, contentDescription = "Studio") },
            label = { Text("Studio", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = StudioPrimaryViolet,
                indicatorColor = StudioPrimaryViolet,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("bottom_nav_studio")
        )

        NavigationBarItem(
            selected = currentTab == 2,
            onClick = { onSelectTab(2) },
            icon = {
                if (savedCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = StudioPrimaryViolet, contentColor = Color.Black) {
                                Text("$savedCount", fontWeight = FontWeight.Bold)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Bookmark, contentDescription = "Saved")
                    }
                } else {
                    Icon(Icons.Default.Bookmark, contentDescription = "Saved")
                }
            },
            label = { Text("Saved", fontSize = 11.sp, fontWeight = FontWeight.SemiBold) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = StudioPrimaryViolet,
                indicatorColor = StudioPrimaryViolet,
                unselectedIconColor = TextMuted,
                unselectedTextColor = TextMuted
            ),
            modifier = Modifier.testTag("bottom_nav_saved")
        )
    }
}

// ---------------------------------------------------------------------------
// ANALYZING STATE CHAMBER
// ---------------------------------------------------------------------------
@Composable
private fun AnalyzingChamber(
    stepMessage: String,
    progress: Float
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = StudioSurface,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorder),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(StudioPrimaryViolet.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = StudioPrimaryViolet,
                        strokeWidth = 3.5.dp,
                        modifier = Modifier.size(46.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = StudioPrimaryViolet,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "AI VIRAL CLIP ANALYSIS",
                    color = StudioPrimaryViolet,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stepMessage,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = StudioPrimaryViolet,
                    trackColor = StudioSurfaceElevated
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${(progress * 100).toInt()}% complete",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// ERROR STATE CHAMBER
// ---------------------------------------------------------------------------
@Composable
private fun ErrorChamber(
    message: String,
    onRetry: () -> Unit,
    onOpenInput: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = StudioSurface,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioCaptionRed.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Analysis Notice",
                    color = StudioCaptionRed,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = message,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onRetry,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioCaptionRed),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Retry", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onOpenInput,
                        colors = ButtonDefaults.buttonColors(containerColor = StudioSurfaceElevated),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Paste Another Link", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// SAVED CLIPS & EXPORT HISTORY VIEW
// ---------------------------------------------------------------------------
@Composable
private fun SavedClipsView(
    savedClips: List<ClipEntity>,
    exportJobs: List<ExportJobEntity>,
    searchQuery: String,
    filterMode: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterChange: (String) -> Unit,
    onToggleFavorite: (String, Boolean) -> Unit,
    onRemove: (String) -> Unit,
    onShowToast: (String) -> Unit,
    onGoToInput: () -> Unit
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var showExportHistory by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 640.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SAVED LIBRARY (${savedClips.size})",
                            color = StudioPrimaryViolet,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )

                        Surface(
                            color = if (showExportHistory) StudioPrimaryViolet else StudioSurfaceElevated,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .clickable { showExportHistory = !showExportHistory }
                                .testTag("toggle_export_history_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Videocam, contentDescription = null, tint = Color.White, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Export History (${exportJobs.size})",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Search Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = { Text("Search by title, creator, or topic...", color = TextMuted, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_saved_clips_field"),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = StudioSurface,
                            unfocusedContainerColor = StudioSurface,
                            focusedBorderColor = StudioPrimaryViolet,
                            unfocusedBorderColor = StudioBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )

                    // Filter Chips Row
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = filterMode == "ALL",
                            onClick = { onFilterChange("ALL") },
                            label = { Text("All Saved", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StudioPrimaryViolet,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = filterMode == "FAVORITES",
                            onClick = { onFilterChange("FAVORITES") },
                            label = { Text("⭐ Favorites", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StudioPrimaryViolet,
                                selectedLabelColor = Color.White
                            )
                        )
                        FilterChip(
                            selected = filterMode == "TOP_SCORE",
                            onClick = { onFilterChange("TOP_SCORE") },
                            label = { Text("🔥 Top Viral", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = StudioPrimaryViolet,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        if (showExportHistory) {
            item {
                Text(
                    text = "MP4 EXPORT QUEUE & RENDER HISTORY",
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }

            if (exportJobs.isEmpty()) {
                item {
                    Surface(
                        color = StudioSurface,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 640.dp)
                    ) {
                        Text(
                            text = "No exported MP4 videos yet. Go to Studio > Export to render clips.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(exportJobs, key = { it.id }) { job ->
                    Surface(
                        color = StudioSurface,
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 640.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = job.outputFileName, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${job.resolution} • ${job.aspectRatio} • ${job.durationSeconds}s • ${(job.fileSizeBytes / 1_000_000.0).format(1)} MB",
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            }
                            Surface(
                                color = Color(0x2210B981),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = job.status,
                                    color = StudioGreen,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Saved Clips List
        if (savedClips.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(42.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (searchQuery.isNotBlank()) "No clips match '$searchQuery'" else "No Saved Clips Yet",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Bookmark candidate moments in the studio to organize your library.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = onGoToInput,
                            colors = ButtonDefaults.buttonColors(containerColor = StudioPrimaryViolet),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Discover Moments", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(savedClips, key = { it.id }) { clip ->
                Surface(
                    color = StudioSurface,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 640.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                color = Color(0x33EF4444),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "${clip.overallScore}% VIRAL",
                                    color = StudioCaptionRed,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${clip.formatRatio} • ${clip.timeRangeFormatted}",
                                    color = StudioPrimaryViolet,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(
                                    onClick = { onToggleFavorite(clip.id, clip.isFavorite) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (clip.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = "Favorite",
                                        tint = if (clip.isFavorite) StudioCaptionYellow else TextMuted,
                                        modifier = Modifier.size(17.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { onRemove(clip.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Remove",
                                        tint = TextMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = clip.clipTitle,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = clip.whyViralExplanation,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                val links = clip.platformLinksJson.split(";")
                                val firstUrl = links.firstOrNull()?.split("|")?.getOrNull(1)
                                    ?: "https://youtu.be/${clip.videoId}?t=${clip.startSeconds}"
                                clipboardManager.setText(androidx.compose.ui.text.AnnotatedString(firstUrl))
                                onShowToast("Timestamp link copied!")
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = StudioSurfaceElevated)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Copy Timestamped Link",
                                color = StudioPrimaryViolet,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Double.format(digits: Int) = "%.${digits}f".format(this)
