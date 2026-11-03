package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SmartDisplay
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.PresetVideo
import com.example.data.PresetVideos
import com.example.model.AspectRatioOption
import com.example.model.FramingMode
import com.example.ui.components.IOSAspectSelectionCard
import com.example.ui.components.IOSGlassCard
import com.example.ui.components.IOSLuminousActionButton
import com.example.ui.theme.StudioPrimaryViolet
import com.example.ui.theme.AppleGreen
import com.example.ui.theme.ApplePurple
import com.example.ui.theme.ApplePurpleGlow
import com.example.ui.theme.AppleRed
import com.example.ui.theme.AppleYellow
import com.example.ui.theme.GlassActiveSpecularBrush
import com.example.ui.theme.GlassSpecularBorderBrush
import com.example.ui.theme.ShadowPurpleGlow
import com.example.ui.theme.ShadowSoftBlack
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.iOSGlassBorder
import com.example.ui.theme.iOSGlassBorderSubtle
import com.example.ui.theme.iOSGlassElevated
import com.example.ui.theme.iOSGlassFill
import com.example.ui.theme.iOSGlassInputBg
import com.example.ui.theme.iOSGlassPill
import com.example.util.YouTubeParser

/**
 * Clean, whitespace-heavy, glassmorphic Main Video Input Screen inspired by iOS 26/27.
 */
@Composable
fun VideoInputView(
    inputUrl: String,
    selectedFormat: AspectRatioOption,
    promptCoPilotQuery: String,
    framingMode: FramingMode,
    isAnalyzing: Boolean,
    onUrlChange: (String) -> Unit,
    onPasteUrl: () -> Unit,
    onClearUrl: () -> Unit,
    onFormatChange: (AspectRatioOption) -> Unit,
    onCoPilotChange: (String) -> Unit,
    onFramingModeChange: (FramingMode) -> Unit,
    onSelectPreset: (PresetVideo) -> Unit,
    onAnalyze: () -> Unit,
    modifier: Modifier = Modifier
) {
    val detectedVideoId = remember(inputUrl) { YouTubeParser.extractVideoId(inputUrl) }
    var isCoPilotExpanded by remember { mutableStateOf(promptCoPilotQuery.isNotEmpty()) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Spacious iOS Hero Header
        IOSHeaderSection()

        Spacer(modifier = Modifier.height(28.dp))

        // Main Glass Input Container Card
        IOSGlassCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = 16.dp,
            ambientColor = ShadowSoftBlack,
            spotColor = ShadowPurpleGlow
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Link,
                        contentDescription = null,
                        tint = StudioPrimaryViolet,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "YOUTUBE VIDEO OR STREAM LINK",
                        color = StudioPrimaryViolet,
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 0.8.sp
                    )
                }

                if (detectedVideoId != null) {
                    Surface(
                        color = Color(0x3330D158),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AppleGreen,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "READY",
                                color = AppleGreen,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Text Input Box with Glass Styling
            OutlinedTextField(
                value = inputUrl,
                onValueChange = onUrlChange,
                placeholder = {
                    Text(
                        text = "https://www.youtube.com/watch?v=...",
                        color = TextMuted,
                        fontSize = 14.sp
                    )
                },
                singleLine = true,
                trailingIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        if (inputUrl.isNotEmpty()) {
                            IconButton(onClick = onClearUrl) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = TextMuted,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Floating Glass "Paste" Pill
                        Surface(
                            color = Color(0x408B5CF6),
                            shape = RoundedCornerShape(14.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ApplePurple.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .clickable { onPasteUrl() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Paste",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("youtube_url_main_input"),
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = StudioPrimaryViolet,
                    unfocusedBorderColor = iOSGlassBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = iOSGlassInputBg,
                    unfocusedContainerColor = iOSGlassInputBg
                )
            )

            // Detected Thumbnail Micro-Preview
            if (detectedVideoId != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x2200D8F6), RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    AsyncImage(
                        model = YouTubeParser.getThumbnailUrl(detectedVideoId),
                        contentDescription = null,
                        modifier = Modifier
                            .size(width = 38.dp, height = 26.dp)
                            .clip(RoundedCornerShape(6.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Video ID: $detectedVideoId verified for high-res analysis",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Target Aspect Ratio Section
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "TARGET VIDEO FORMAT",
                    color = TextPrimary,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 0.6.sp
                )

                Text(
                    text = "Auto-Reframed by AI",
                    color = ApplePurple,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AspectRatioOption.values().forEach { option ->
                    val isSelected = selectedFormat == option
                    IOSAspectSelectionCard(
                        option = option,
                        isSelected = isSelected,
                        onSelect = { onFormatChange(option) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("aspect_card_${option.label.replace(':', '_')}")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Intent-Based Prompt-to-Clip Co-Pilot Glass Card
        IOSGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = spring(dampingRatio = 0.8f, stiffness = Spring.StiffnessMediumLow)),
            shape = RoundedCornerShape(24.dp),
            elevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isCoPilotExpanded = !isCoPilotExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .background(Color(0x338B5CF6), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = ApplePurple,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Intent Prompt Co-Pilot",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        Text(
                            text = if (promptCoPilotQuery.isEmpty()) "Guide AI to specific highlights" else "Active: \"$promptCoPilotQuery\"",
                            color = if (promptCoPilotQuery.isEmpty()) TextMuted else ApplePurple,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    color = if (isCoPilotExpanded) ApplePurple else iOSGlassPill,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = if (isCoPilotExpanded) "Collapse" else "Customize",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            AnimatedVisibility(visible = isCoPilotExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    OutlinedTextField(
                        value = promptCoPilotQuery,
                        onValueChange = onCoPilotChange,
                        placeholder = {
                            Text(
                                text = "e.g., 'Find all moments Speed screams or breaks monitor' or 'Extract AI quotes'",
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("prompt_copilot_input"),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ApplePurple,
                            unfocusedBorderColor = iOSGlassBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = iOSGlassInputBg,
                            unfocusedContainerColor = iOSGlassInputBg
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Suggestion Pills
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "🔥 Rage & Screams",
                            "😂 Laughs & Fails",
                            "🧠 Deep Insights",
                            "🚨 Climax Plot Hook"
                        ).forEach { tag ->
                            Surface(
                                color = iOSGlassPill,
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, iOSGlassBorderSubtle),
                                modifier = Modifier.clickable { onCoPilotChange(tag) }
                            ) {
                                Text(
                                    text = tag,
                                    color = ApplePurple,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // AI Visual Framing Mode Chips
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CenterFocusStrong,
                    contentDescription = null,
                    tint = StudioPrimaryViolet,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AI Visual Framing Mode:",
                    color = TextSecondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FramingMode.values().forEach { mode ->
                    val isModeSelected = framingMode == mode
                    FilterChip(
                        selected = isModeSelected,
                        onClick = { onFramingModeChange(mode) },
                        label = { Text(text = mode.displayName, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = StudioPrimaryViolet,
                            selectedLabelColor = Color.Black,
                            containerColor = iOSGlassPill,
                            labelColor = TextSecondary
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isModeSelected,
                            borderColor = if (isModeSelected) StudioPrimaryViolet else iOSGlassBorderSubtle
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Big Primary Action Button
        IOSLuminousActionButton(
            text = "Analyze & Generate Viral Clips",
            icon = Icons.Default.AutoAwesome,
            isLoading = isAnalyzing,
            onClick = onAnalyze,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("analyze_video_button")
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Trending Streams Carousel
        IOSPresetsSection(onSelectPreset = onSelectPreset)

        Spacer(modifier = Modifier.height(24.dp))
    }
}

/**
 * Clean, modern iOS 27 Header with expansive whitespace.
 */
@Composable
private fun IOSHeaderSection() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pill Badge
        Surface(
            color = Color(0x338B5CF6),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ApplePurple.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(StudioPrimaryViolet, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NEXT-GEN AI VIRAL ENGINE",
                    color = ApplePurpleGlow,
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Turn Long Videos\nInto Viral Gold",
            color = TextPrimary,
            fontWeight = FontWeight.Black,
            fontSize = 26.sp,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp,
            letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Paste any YouTube link to detect viral hooks, predict audience retention, auto-frame faces, and style kinetic captions.",
            color = TextSecondary,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
    }
}

/**
 * Trending Streams Carousel with glass preview cards.
 */
@Composable
private fun IOSPresetsSection(
    onSelectPreset: (PresetVideo) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "OR EXPLORE TRENDING STREAMS",
                color = StudioPrimaryViolet,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                letterSpacing = 0.8.sp
            )

            Text(
                text = "Instant 1-Tap Load",
                color = TextMuted,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(PresetVideos.list) { preset ->
                Surface(
                    color = iOSGlassElevated,
                    shape = RoundedCornerShape(18.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, GlassSpecularBorderBrush),
                    modifier = Modifier
                        .width(200.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onSelectPreset(preset) }
                        .testTag("preset_pill_${preset.id}")
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(95.dp)
                        ) {
                            AsyncImage(
                                model = preset.thumbnailUrl,
                                contentDescription = preset.title,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                            Surface(
                                color = Color(0xCC000000),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(6.dp)
                            ) {
                                Text(
                                    text = preset.durationFormatted,
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = preset.title,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = preset.channel,
                                color = StudioPrimaryViolet,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Surface(
                                color = Color(0x338B5CF6),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = preset.category.substringBefore(" "),
                                    color = ApplePurple,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
