package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.AspectRatioOption
import com.example.model.CaptionPosition
import com.example.model.CaptionStyleType
import com.example.model.FramingMode
import com.example.model.ViralMomentClip
import com.example.ui.theme.StudioBorder
import com.example.ui.theme.StudioBorderSubtle
import com.example.ui.theme.StudioCaptionYellow
import com.example.ui.theme.StudioPrimaryViolet
import com.example.ui.theme.StudioPrimaryVioletLight
import com.example.ui.theme.StudioSurface
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.StudioSurfaceSubtle
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.util.YouTubeParser
import kotlin.math.roundToInt

/**
 * Premium, cinematic video preview with interactive draggable start/end trim handles.
 * Features a static frame preview at the exact timestamp as the user drags the handles.
 * Sophisticated smoky-glass & graphite studio aesthetic with elegant soft-violet accents.
 */
@Composable
fun VideoPlayerPreview(
    clip: ViralMomentClip,
    videoId: String,
    thumbnailUrl: String,
    aspectRatio: AspectRatioOption,
    captionStyle: CaptionStyleType,
    captionPosition: CaptionPosition,
    framingMode: FramingMode,
    fontSizeSp: Float,
    isPlaying: Boolean,
    currentTimeSeconds: Float,
    showSafeZones: Boolean,
    onFormatChange: (AspectRatioOption) -> Unit,
    onTogglePlay: () -> Unit,
    onSeek: (Float) -> Unit,
    onToggleSafeZones: () -> Unit,
    onBookmarkToggle: () -> Unit,
    isBookmarked: Boolean,
    onNudgeStart: ((Float) -> Unit)? = null,
    onNudgeEnd: ((Float) -> Unit)? = null,
    onUpdateBounds: ((newStart: Int, newEnd: Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val duration = clip.durationSeconds.toFloat().coerceAtLeast(1f)
    var useLiveYouTubePlayer by remember { mutableStateOf(false) }
    var showFaceTrackOverlay by remember { mutableStateOf(false) }

    // Live frame inspection state during trimming drag
    var activeDragTimestamp by remember { mutableStateOf<Int?>(null) }
    var activeDragType by remember { mutableStateOf<String?>(null) } // "START" or "END"

    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 520.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---------------------------------------------------------------------
        // 1. Sleek Format & Tool Strip (Smoky Glass)
        // ---------------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Aspect Ratio Segmented Control (9:16, 1:1, 16:9)
            Surface(
                color = StudioSurface,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle)
            ) {
                Row(
                    modifier = Modifier.padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AspectRatioOption.values().forEach { option ->
                        val isSelected = aspectRatio == option
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) StudioPrimaryViolet else Color.Transparent)
                                .clickable { onFormatChange(option) }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = option.label,
                                color = if (isSelected) Color.Black else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Quick Canvas Controls (Safe Zones, Face Track, Live Mode)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Safe Zone Guide Toggle
                Surface(
                    color = if (showSafeZones) StudioPrimaryViolet.copy(alpha = 0.2f) else StudioSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (showSafeZones) StudioPrimaryViolet else StudioBorderSubtle
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onToggleSafeZones() }
                        .testTag("safe_zone_toggle_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridOn,
                            contentDescription = "Safe Zones",
                            tint = if (showSafeZones) StudioPrimaryViolet else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Guides",
                            color = if (showSafeZones) TextPrimary else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Face Track Box Toggle
                Surface(
                    color = if (showFaceTrackOverlay) StudioPrimaryViolet.copy(alpha = 0.2f) else StudioSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (showFaceTrackOverlay) StudioPrimaryViolet else StudioBorderSubtle
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { showFaceTrackOverlay = !showFaceTrackOverlay }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CenterFocusStrong,
                            contentDescription = "Face Tracking Overlay",
                            tint = if (showFaceTrackOverlay) StudioPrimaryViolet else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Track",
                            color = if (showFaceTrackOverlay) TextPrimary else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Live Player Toggle
                Surface(
                    color = if (useLiveYouTubePlayer) StudioPrimaryViolet.copy(alpha = 0.25f) else StudioSurface,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (useLiveYouTubePlayer) StudioPrimaryViolet else StudioBorderSubtle
                    ),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { useLiveYouTubePlayer = !useLiveYouTubePlayer }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.SmartDisplay,
                            contentDescription = "Live YouTube Player",
                            tint = if (useLiveYouTubePlayer) StudioPrimaryViolet else TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }

        // ---------------------------------------------------------------------
        // 2. Main Video Viewport (Refined Charcoal Framing)
        // ---------------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth(if (aspectRatio == AspectRatioOption.RATIO_9_16) 0.82f else 0.98f)
                .aspectRatio(aspectRatio.ratioValue)
                .clip(RoundedCornerShape(18.dp))
                .border(
                    width = if (activeDragTimestamp != null) 2.dp else 1.dp,
                    color = if (activeDragTimestamp != null) StudioPrimaryViolet else StudioBorder,
                    shape = RoundedCornerShape(18.dp)
                )
                .background(Color(0xFF0A0A0A)),
            contentAlignment = Alignment.Center
        ) {
            if (activeDragTimestamp != null) {
                // -----------------------------------------------------------------
                // Dynamic Static Frame Inspection Viewport at exact dragged timestamp
                // -----------------------------------------------------------------
                val staticFrameUrl = YouTubeParser.getFramePreviewUrl(videoId, activeDragTimestamp!!)
                AsyncImage(
                    model = staticFrameUrl,
                    contentDescription = "Static Frame Preview at ${YouTubeParser.formatSecondsToTime(activeDragTimestamp!!)}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Trimming HUD Badge
                Surface(
                    color = Color(0xEE121212),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, StudioPrimaryViolet),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(StudioPrimaryViolet, CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TRIM ${if (activeDragType == "START") "START" else "END"} FRAME: ${YouTubeParser.formatSecondsToTime(activeDragTimestamp!!)}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Subtitle Line at this trimmed boundary if available
                val activeCaption = clip.captions.find {
                    activeDragTimestamp!!.toFloat() in it.startSeconds..it.endSeconds
                }
                if (activeCaption != null) {
                    Surface(
                        color = Color(0xD9101010),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 14.dp, start = 14.dp, end = 14.dp)
                    ) {
                        Text(
                            text = "\"${activeCaption.text}\"",
                            color = StudioPrimaryVioletLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else if (useLiveYouTubePlayer) {
                // Real Live YouTube Player (embedded iframe)
                RealYouTubePlayer(
                    videoId = videoId,
                    startSeconds = clip.startSeconds,
                    endSeconds = clip.endSeconds,
                    isPlaying = isPlaying,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Multi-Speaker Stacked Framing or Full Canvas
                if (framingMode == FramingMode.SPLIT_SCREEN_STACKED && aspectRatio == AspectRatioOption.RATIO_9_16) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onTogglePlay() }
                    ) {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                            AsyncImage(
                                model = thumbnailUrl,
                                contentDescription = "Speaker A",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                color = Color(0xCC141414),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = "SPEAKER A",
                                    color = StudioPrimaryViolet,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .background(StudioPrimaryViolet)
                        )
                        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                            AsyncImage(
                                model = thumbnailUrl,
                                contentDescription = "Speaker B",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Surface(
                                color = Color(0xCC141414),
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                            ) {
                                Text(
                                    text = "SPEAKER B",
                                    color = StudioCaptionYellow,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                } else {
                    // Single Speaker with Dynamic Framing
                    AsyncImage(
                        model = thumbnailUrl,
                        contentDescription = "Video Frame",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable { onTogglePlay() }
                    )
                }

                // AI Speaker Face Tracking Box Overlay
                if (showFaceTrackOverlay && isPlaying) {
                    Box(
                        modifier = Modifier
                            .size(width = 110.dp, height = 140.dp)
                            .border(1.5.dp, StudioPrimaryViolet.copy(alpha = 0.85f), RoundedCornerShape(8.dp))
                            .background(StudioPrimaryViolet.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                            .align(Alignment.Center)
                    ) {
                        Surface(
                            color = Color(0xCC141414),
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 4.dp)
                        ) {
                            Text(
                                text = "FACE TRACK 99%",
                                color = StudioPrimaryViolet,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                // Subtle Cinematic Vignette
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x33000000),
                                    Color.Transparent,
                                    Color(0x55000000)
                                )
                            )
                        )
                )

                // Dynamic Kinetic Captions Overlay
                CaptionOverlay(
                    captions = clip.captions,
                    currentTimeSeconds = currentTimeSeconds,
                    styleType = captionStyle,
                    position = captionPosition,
                    fontSizeSp = fontSizeSp
                )

                // Minimal Safe Zone Guidelines
                if (showSafeZones && aspectRatio == AspectRatioOption.RATIO_9_16) {
                    CleanSafeZoneGuides()
                }

                // Center Play/Pause Indicator (Shown when paused)
                if (!isPlaying && !useLiveYouTubePlayer) {
                    Surface(
                        color = Color(0x99141414),
                        shape = CircleShape,
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0x33FFFFFF)),
                        modifier = Modifier
                            .size(54.dp)
                            .align(Alignment.Center)
                            .clickable { onTogglePlay() }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ---------------------------------------------------------------------
        // 3. Professional Editing Timeline & Interactive Draggable Trimming
        // ---------------------------------------------------------------------
        Surface(
            color = StudioSurface,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Row A: Playhead Time, Play/Pause Button & Duration Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onTogglePlay,
                            modifier = Modifier
                                .size(34.dp)
                                .background(StudioPrimaryViolet, CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        // High-contrast monospace playhead clock
                        Text(
                            text = String.format("%04.1fs / %04.1fs", currentTimeSeconds, duration),
                            color = TextPrimary,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Status Indicator Badge
                    Surface(
                        color = if (isPlaying) StudioPrimaryViolet.copy(alpha = 0.15f) else StudioSurfaceElevated,
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isPlaying) StudioPrimaryViolet.copy(alpha = 0.5f) else StudioBorderSubtle
                        )
                    ) {
                        Text(
                            text = if (isPlaying) "PLAYING" else "PAUSED",
                            color = if (isPlaying) StudioPrimaryViolet else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // -----------------------------------------------------------------
                // Interactive Draggable Filmstrip Timeline with Dual Handles & Frame Loupe
                // -----------------------------------------------------------------
                DraggableTimelineTrimmer(
                    videoId = videoId,
                    startSeconds = clip.startSeconds,
                    endSeconds = clip.endSeconds,
                    currentTimeSeconds = currentTimeSeconds,
                    onBoundsChange = { newStart, newEnd ->
                        if (onUpdateBounds != null) {
                            onUpdateBounds(newStart, newEnd)
                        } else {
                            if (newStart != clip.startSeconds) onNudgeStart?.invoke((newStart - clip.startSeconds).toFloat())
                            if (newEnd != clip.endSeconds) onNudgeEnd?.invoke((newEnd - clip.endSeconds).toFloat())
                        }
                    },
                    onSeek = onSeek,
                    onDragStateChange = { timestamp, type ->
                        activeDragTimestamp = timestamp
                        activeDragType = type
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Row B: Fine-Tuning Micro-Nudge Buttons (-0.5s / +0.5s)
                if (onNudgeStart != null && onNudgeEnd != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // In-Point Trimmer
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Start: ${YouTubeParser.formatSecondsToTime(clip.startSeconds)}",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = StudioSurfaceElevated,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onNudgeStart(-0.5f) }
                                    .testTag("nudge_start_minus_button")
                            ) {
                                Text(
                                    text = "-0.5s",
                                    color = StudioPrimaryViolet,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                color = StudioSurfaceElevated,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onNudgeStart(0.5f) }
                                    .testTag("nudge_start_plus_button")
                            ) {
                                Text(
                                    text = "+0.5s",
                                    color = StudioPrimaryViolet,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // Out-Point Trimmer
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "End: ${YouTubeParser.formatSecondsToTime(clip.endSeconds)}",
                                color = TextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = StudioSurfaceElevated,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onNudgeEnd(-0.5f) }
                                    .testTag("nudge_end_minus_button")
                            ) {
                                Text(
                                    text = "-0.5s",
                                    color = StudioPrimaryViolet,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                color = StudioSurfaceElevated,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, StudioBorderSubtle),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { onNudgeEnd(0.5f) }
                                    .testTag("nudge_end_plus_button")
                            ) {
                                Text(
                                    text = "+0.5s",
                                    color = StudioPrimaryViolet,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Interactive Draggable Dual-Handle Visual Filmstrip Timeline.
 * Renders an exact static frame loupe preview card as handles are dragged.
 */
@Composable
private fun DraggableTimelineTrimmer(
    videoId: String,
    startSeconds: Int,
    endSeconds: Int,
    currentTimeSeconds: Float,
    onBoundsChange: (newStart: Int, newEnd: Int) -> Unit,
    onSeek: (Float) -> Unit,
    onDragStateChange: (timestamp: Int?, type: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    // Sliding display window around clip boundaries
    val windowMargin = 15
    val windowStart = (startSeconds - windowMargin).coerceAtLeast(0)
    val windowEnd = (endSeconds + windowMargin).coerceAtLeast(windowStart + 40)
    val windowDuration = (windowEnd - windowStart).toFloat().coerceAtLeast(1f)

    var isDraggingStart by remember { mutableStateOf(false) }
    var isDraggingEnd by remember { mutableStateOf(false) }

    // Live display values during drag
    var liveStart by remember(startSeconds) { mutableIntStateOf(startSeconds) }
    var liveEnd by remember(endSeconds) { mutableIntStateOf(endSeconds) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Drag Feedback Header (Shows live timecode when dragging)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isDraggingStart) "PREVIEWING IN-POINT FRAME"
                else if (isDraggingEnd) "PREVIEWING OUT-POINT FRAME"
                else "DRAG HANDLES TO TRIM",
                color = if (isDraggingStart || isDraggingEnd) StudioPrimaryViolet else TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Text(
                text = "${YouTubeParser.formatSecondsToTime(liveStart)} ➔ ${YouTubeParser.formatSecondsToTime(liveEnd)} (${liveEnd - liveStart}s)",
                color = if (isDraggingStart || isDraggingEnd) StudioPrimaryViolet else TextSecondary,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Loupe & time indicator headroom wrapper
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 92.dp)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                val trackWidthPx = constraints.maxWidth.toFloat().coerceAtLeast(1f)

                // 1. Filmstrip background track
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF141414))
                        .border(1.dp, StudioBorderSubtle, RoundedCornerShape(8.dp))
                        .pointerInput(windowStart, windowDuration, liveStart, liveEnd) {
                            detectTapGestures { offset ->
                                val trackWidth = size.width.toFloat().coerceAtLeast(1f)
                                val tappedRatio = (offset.x / trackWidth).coerceIn(0f, 1f)
                                val tappedSecond = windowStart + (tappedRatio * windowDuration)
                                if (tappedSecond in liveStart.toFloat()..liveEnd.toFloat()) {
                                    onSeek(tappedSecond - liveStart)
                                }
                            }
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val tickStep = 10.dp.toPx()
                        var currentX = 0f
                        while (currentX < size.width) {
                            drawLine(
                                color = Color(0x18FFFFFF),
                                start = Offset(currentX, 0f),
                                end = Offset(currentX, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                            currentX += tickStep
                        }
                    }
                }

                // Calculate ratios for Start and End positions
                val startRatio = ((liveStart - windowStart) / windowDuration).coerceIn(0f, 1f)
                val endRatio = ((liveEnd - windowStart) / windowDuration).coerceIn(startRatio + 0.05f, 1f)

                val startPx = startRatio * trackWidthPx
                val endPx = endRatio * trackWidthPx
                val activeWidthPx = (endPx - startPx).coerceAtLeast(20f)

                val startDp = with(density) { startPx.toDp() }
                val activeWidthDp = with(density) { activeWidthPx.toDp() }

                // 2. Highlighted Selected Region
                Box(
                    modifier = Modifier
                        .offset(x = startDp)
                        .width(activeWidthDp)
                        .fillMaxSize()
                        .background(StudioPrimaryViolet.copy(alpha = 0.16f))
                        .border(
                            width = 1.5.dp,
                            color = StudioPrimaryViolet.copy(alpha = 0.75f)
                        )
                )

                // 3. Playhead Scrubbing Needle
                val clipDuration = (liveEnd - liveStart).toFloat().coerceAtLeast(1f)
                val playheadRel = (currentTimeSeconds / clipDuration).coerceIn(0f, 1f)
                val playheadPx = startPx + (playheadRel * activeWidthPx)
                val playheadDp = with(density) { (playheadPx - 1.dp.toPx()).toDp() }

                Box(
                    modifier = Modifier
                        .offset(x = playheadDp)
                        .width(2.dp)
                        .fillMaxSize()
                        .background(Color.White)
                )

                // 4. Left Draggable Trim Handle (Start In-Point)
                val handleWidthDp = 22.dp
                val handleWidthPx = with(density) { handleWidthDp.toPx() }
                val leftHandleOffsetDp = with(density) { (startPx - (handleWidthPx / 2)).coerceAtLeast(0f).toDp() }

                Box(
                    modifier = Modifier
                        .offset(x = leftHandleOffsetDp)
                        .width(handleWidthDp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                        .background(if (isDraggingStart) StudioPrimaryVioletLight else StudioPrimaryViolet)
                        .pointerInput(liveEnd, windowStart, windowDuration, trackWidthPx) {
                            detectHorizontalDragGestures(
                                onDragStart = {
                                    isDraggingStart = true
                                    onDragStateChange(liveStart, "START")
                                },
                                onDragEnd = {
                                    isDraggingStart = false
                                    onDragStateChange(null, null)
                                    onBoundsChange(liveStart, liveEnd)
                                },
                                onDragCancel = {
                                    isDraggingStart = false
                                    onDragStateChange(null, null)
                                },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    val deltaSeconds = (dragAmount / trackWidthPx) * windowDuration
                                    val candidateStart = (liveStart + deltaSeconds).roundToInt()
                                    val safeStart = candidateStart.coerceIn(windowStart, liveEnd - 5)
                                    liveStart = safeStart
                                    onDragStateChange(safeStart, "START")
                                    onBoundsChange(safeStart, liveEnd)
                                }
                            )
                        }
                        .testTag("draggable_trim_handle_start"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(width = 3.dp, height = 12.dp).background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(1.dp)))
                }

                // 5. Right Draggable Trim Handle (End Out-Point)
                val rightHandleOffsetDp = with(density) { (endPx - (handleWidthPx / 2)).coerceAtMost(trackWidthPx - handleWidthPx).toDp() }

                Box(
                    modifier = Modifier
                        .offset(x = rightHandleOffsetDp)
                        .width(handleWidthDp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp))
                        .background(if (isDraggingEnd) StudioPrimaryVioletLight else StudioPrimaryViolet)
                        .pointerInput(liveStart, windowStart, windowDuration, trackWidthPx) {
                            detectHorizontalDragGestures(
                                onDragStart = {
                                    isDraggingEnd = true
                                    onDragStateChange(liveEnd, "END")
                                },
                                onDragEnd = {
                                    isDraggingEnd = false
                                    onDragStateChange(null, null)
                                    onBoundsChange(liveStart, liveEnd)
                                },
                                onDragCancel = {
                                    isDraggingEnd = false
                                    onDragStateChange(null, null)
                                },
                                onHorizontalDrag = { change, dragAmount ->
                                    change.consume()
                                    val deltaSeconds = (dragAmount / trackWidthPx) * windowDuration
                                    val candidateEnd = (liveEnd + deltaSeconds).roundToInt()
                                    val safeEnd = candidateEnd.coerceIn(liveStart + 5, windowEnd)
                                    liveEnd = safeEnd
                                    onDragStateChange(safeEnd, "END")
                                    onBoundsChange(liveStart, safeEnd)
                                }
                            )
                        }
                        .testTag("draggable_trim_handle_end"),
                    contentAlignment = Alignment.Center
                ) {
                    Box(modifier = Modifier.size(width = 3.dp, height = 12.dp).background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(1.dp)))
                }

                // 6. Precise Numerical Time Indicators (00:00 format) above Trim Handles
                val indicatorWidthDp = 46.dp
                val indicatorWidthPx = with(density) { indicatorWidthDp.toPx() }
                val startIndicatorXPx = (startPx - (indicatorWidthPx / 2)).coerceIn(0f, trackWidthPx - indicatorWidthPx)
                val endIndicatorXPx = (endPx - (indicatorWidthPx / 2)).coerceIn(0f, trackWidthPx - indicatorWidthPx)

                val overlap = (startIndicatorXPx + indicatorWidthPx + 4f) > endIndicatorXPx
                val adjustedStartXPx = if (overlap) (startPx - indicatorWidthPx).coerceAtLeast(0f) else startIndicatorXPx
                val adjustedEndXPx = if (overlap) endPx.coerceAtMost(trackWidthPx - indicatorWidthPx) else endIndicatorXPx

                TrimHandleTimeIndicator(
                    timeFormatted = YouTubeParser.formatSecondsToTime(liveStart),
                    isActive = isDraggingStart,
                    label = "START",
                    modifier = Modifier
                        .offset(
                            x = with(density) { adjustedStartXPx.toDp() },
                            y = (-26).dp
                        )
                        .testTag("trim_handle_start_time_indicator")
                )

                TrimHandleTimeIndicator(
                    timeFormatted = YouTubeParser.formatSecondsToTime(liveEnd),
                    isActive = isDraggingEnd,
                    label = "END",
                    modifier = Modifier
                        .offset(
                            x = with(density) { adjustedEndXPx.toDp() },
                            y = (-26).dp
                        )
                        .testTag("trim_handle_end_time_indicator")
                )

                // 7. Floating Static Frame Loupe Preview
                val loupeWidthPx = with(density) { 108.dp.toPx() }
                if (isDraggingStart) {
                    val loupeXPx = (startPx - (loupeWidthPx / 2)).coerceIn(0f, trackWidthPx - loupeWidthPx)
                    HandleLoupePreview(
                        videoId = videoId,
                        timestampSeconds = liveStart,
                        label = "IN-POINT",
                        modifier = Modifier
                            .offset(
                                x = with(density) { loupeXPx.toDp() },
                                y = (-86).dp
                            )
                    )
                } else if (isDraggingEnd) {
                    val loupeXPx = (endPx - (loupeWidthPx / 2)).coerceIn(0f, trackWidthPx - loupeWidthPx)
                    HandleLoupePreview(
                        videoId = videoId,
                        timestampSeconds = liveEnd,
                        label = "OUT-POINT",
                        modifier = Modifier
                            .offset(
                                x = with(density) { loupeXPx.toDp() },
                                y = (-86).dp
                            )
                    )
                }
            }
        }
    }
}

/**
 * Precise numerical time indicator in 00:00 format directly above trim handles.
 */
@Composable
private fun TrimHandleTimeIndicator(
    timeFormatted: String,
    isActive: Boolean,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = if (isActive) StudioPrimaryViolet else Color(0xFA181818),
            shape = RoundedCornerShape(5.dp),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isActive) StudioPrimaryVioletLight else StudioBorder
            ),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = timeFormatted,
                    color = if (isActive) Color.Black else TextPrimary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 0.5.sp
                )
            }
        }
        // Micro indicator caret pointing to the handle top edge
        Box(
            modifier = Modifier
                .size(width = 2.dp, height = 3.dp)
                .background(if (isActive) StudioPrimaryViolet else StudioBorder)
        )
    }
}

/**
 * Floating Picture-in-Picture Frame Preview Card (Loupe).
 * Shows the exact video frame at the dragged timestamp directly above the active handle.
 */
@Composable
private fun HandleLoupePreview(
    videoId: String,
    timestampSeconds: Int,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = Color(0xFA141414),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, StudioPrimaryViolet),
            shadowElevation = 10.dp
        ) {
            Column(
                modifier = Modifier.padding(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 100.dp, height = 56.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.Black)
                ) {
                    AsyncImage(
                        model = YouTubeParser.getFramePreviewUrl(videoId, timestampSeconds),
                        contentDescription = "Trim Frame at ${YouTubeParser.formatSecondsToTime(timestampSeconds)}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Top label tag
                    Surface(
                        color = Color(0xCC000000),
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.TopStart)
                    ) {
                        Text(
                            text = label,
                            color = StudioPrimaryViolet,
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                    // Bottom timecode pill
                    Surface(
                        color = Color(0xDD000000),
                        shape = RoundedCornerShape(3.dp),
                        modifier = Modifier
                            .padding(3.dp)
                            .align(Alignment.BottomEnd)
                    ) {
                        Text(
                            text = YouTubeParser.formatSecondsToTime(timestampSeconds),
                            color = Color.White,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            }
        }

        // Downward pointer caret
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            tint = StudioPrimaryViolet,
            modifier = Modifier
                .size(16.dp)
                .offset(y = (-4).dp)
        )
    }
}

/**
 * Clean, subtle safe zone guidelines for TikTok / Reels.
 * Shows dashed bounding margins without obstructing video content.
 */
@Composable
private fun CleanSafeZoneGuides() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 20.dp)
            .border(
                1.dp,
                StudioPrimaryViolet.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
    ) {
        Surface(
            color = Color(0xCC141414),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 6.dp)
        ) {
            Text(
                text = "TIKTOK / REELS SAFE MARGIN",
                color = StudioPrimaryViolet,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
}
