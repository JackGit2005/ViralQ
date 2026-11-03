package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.HookABVariation
import com.example.model.SocialSchedulePlan
import com.example.model.ThumbnailVariant
import com.example.model.ViralMomentClip
import com.example.ui.theme.StudioPrimaryViolet
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.StudioCardBg
import com.example.ui.theme.StudioOutline
import com.example.ui.theme.StudioSurfaceElevated
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.ViralFlame
import com.example.ui.theme.ViralGreen
import com.example.ui.theme.ViralYellow

@Composable
fun HookAndSchedulerCard(
    clip: ViralMomentClip,
    onShowToast: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    var queuedPlatforms by remember { mutableStateOf(setOf<String>()) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioOutline)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header: Multi-Hook A/B Variations
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Campaign,
                    contentDescription = null,
                    tint = ViralYellow,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "3-STYLE HOOK A/B TESTING (FIRST 3 SECONDS)",
                    color = ViralYellow,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Test these 3 structural intro variations to maximize viewer hook retention:",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Hook Variations List
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                clip.hookVariations.forEach { hook ->
                    HookVariationItem(
                        hook = hook,
                        onCopy = {
                            clipboardManager.setText(AnnotatedString(hook.onScreenText))
                            onShowToast("Copied hook text: ${hook.onScreenText}")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Trend-Aware Sound Match & SFX Accents
            Surface(
                color = StudioSurfaceElevated,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioOutline)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = NeonViolet,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "TREND-AWARE AUDIO & SFX MATCHING",
                            color = NeonViolet,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Matched Sentiment:", color = TextMuted, fontSize = 10.sp)
                            Text(text = clip.soundVibeMatch.sentiment, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Trending Sound Vibe:", color = TextMuted, fontSize = 10.sp)
                            Text(text = clip.soundVibeMatch.audioVibe, color = StudioPrimaryViolet, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Accent SFX Injected: ${clip.soundVibeMatch.sfxTriggers.joinToString(" • ")}",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // A/B Thumbnail Testing Generator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = null,
                    tint = StudioPrimaryViolet,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "A/B THUMBNAIL TESTING GENERATOR",
                    color = StudioPrimaryViolet,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                clip.thumbnailVariants.forEach { variant ->
                    ThumbnailVariantItem(
                        variant = variant,
                        onCopyOverlay = {
                            clipboardManager.setText(AnnotatedString(variant.overlayText))
                            onShowToast("Copied thumbnail text: ${variant.overlayText}")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Automated Multi-Platform Social Media Scheduler
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = null,
                    tint = ViralGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "AUTOMATED SOCIAL MEDIA SCHEDULER",
                    color = ViralGreen,
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "AI-calculated optimal posting windows for maximum algorithmic reach:",
                color = TextSecondary,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                clip.schedulePlans.forEach { plan ->
                    val isQueued = queuedPlatforms.contains(plan.platform)
                    SchedulePlanItem(
                        plan = plan,
                        isQueued = isQueued,
                        onToggleQueue = {
                            queuedPlatforms = if (isQueued) {
                                queuedPlatforms - plan.platform
                            } else {
                                onShowToast("Queued for ${plan.platform} (${plan.optimalTimeWindow})!")
                                queuedPlatforms + plan.platform
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HookVariationItem(
    hook: HookABVariation,
    onCopy: () -> Unit
) {
    Surface(
        color = StudioSurfaceElevated,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioOutline)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = hook.title,
                    color = ViralYellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Surface(
                    color = Color(0x3310B981),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = hook.predictedRetentionBonus,
                        color = ViralGreen,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "\"${hook.onScreenText}\"",
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onCopy, modifier = Modifier.size(28.dp)) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Hook",
                        tint = StudioPrimaryViolet,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "First 3s Action: ${hook.first3SecondsAction}",
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun ThumbnailVariantItem(
    variant: ThumbnailVariant,
    onCopyOverlay: () -> Unit
) {
    Surface(
        color = StudioSurfaceElevated,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioOutline)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = variant.label,
                        color = Color(variant.headlineColor),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = variant.predictedCtr,
                        color = ViralYellow,
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = variant.overlayText,
                    color = TextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = variant.rationale,
                    color = TextMuted,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }

            IconButton(onClick = onCopyOverlay, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy text",
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun SchedulePlanItem(
    plan: SocialSchedulePlan,
    isQueued: Boolean,
    onToggleQueue: () -> Unit
) {
    Surface(
        color = StudioSurfaceElevated,
        shape = RoundedCornerShape(10.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isQueued) ViralGreen else StudioOutline
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plan.platform,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "• ${plan.optimalTimeWindow}",
                        color = StudioPrimaryViolet,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Peak Days: ${plan.peakActivityDay}",
                    color = TextMuted,
                    fontSize = 10.sp
                )
                Text(
                    text = plan.algorithmRationale,
                    color = TextSecondary,
                    fontSize = 10.sp,
                    lineHeight = 14.sp
                )
            }

            Button(
                onClick = onToggleQueue,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isQueued) Color(0xFF065F46) else Color(0x338B5CF6),
                    contentColor = if (isQueued) Color(0xFF34D399) else NeonViolet
                ),
                modifier = Modifier.padding(start = 6.dp)
            ) {
                if (isQueued) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Queued", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.Send, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Queue", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
