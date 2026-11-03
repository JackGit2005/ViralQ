package com.example.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ViralMetrics
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
import com.example.ui.theme.ViralYellow

@Composable
fun ViralScoreCard(
    clip: ViralMomentClip,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = StudioCardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, StudioOutline)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            // Header Score Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = null,
                            tint = ViralFlame,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AI VIRAL POTENTIAL",
                            color = ViralFlame,
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    Text(
                        text = getScoreTier(clip.metrics.overallScore),
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

                // Score Badge Circle
                Box(
                    modifier = Modifier
                        .size(62.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFE11D48), Color(0xFFF59E0B))
                            )
                        )
                        .padding(3.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0F172A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${clip.metrics.overallScore}",
                                color = ViralYellow,
                                fontWeight = FontWeight.Black,
                                fontSize = 20.sp,
                                lineHeight = 20.sp
                            )
                            Text(
                                text = "/100",
                                color = TextMuted,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(14.dp))

            // Transparent 5-Factor Weighted Metrics Breakdown
            Text(
                text = "TRANSPARENT VIRAL HEURISTIC BREAKDOWN",
                color = TextSecondary,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                MetricProgressBar(
                    label = "Hook Strength (30% weight)",
                    score = clip.metrics.hookScore,
                    icon = Icons.Default.ElectricBolt,
                    color = Color(0xFF06B6D4)
                )
                MetricProgressBar(
                    label = "Emotional Intensity (25% weight)",
                    score = clip.metrics.emotionalIntensity,
                    icon = Icons.Default.AutoAwesome,
                    color = Color(0xFFEF4444)
                )
                MetricProgressBar(
                    label = "Narrative Payoff (20% weight)",
                    score = clip.metrics.narrativePayoff,
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF8B5CF6)
                )
                MetricProgressBar(
                    label = "Standalone Context (15% weight)",
                    score = clip.metrics.standaloneContext,
                    icon = Icons.Default.Psychology,
                    color = Color(0xFF10B981)
                )
                MetricProgressBar(
                    label = "Shareability (10% weight)",
                    score = clip.metrics.shareabilityScore,
                    icon = Icons.Default.EmojiEvents,
                    color = Color(0xFFFACC15)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Editorial Estimate Disclaimer (MANDATORY REQUIREMENT)
            Surface(
                color = StudioSurfaceElevated,
                shape = RoundedCornerShape(10.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, StudioOutline)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "ℹ️",
                        fontSize = 12.sp,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = "Editorial Estimate: Viral Score is an AI heuristic calculated from hook retention, emotional spikes, narrative structure, and clarity. It is an editorial assessment, not measured views.",
                        color = TextMuted,
                        fontSize = 10.sp,
                        lineHeight = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Why It Will Go Viral" In-Depth Analysis Box
            Surface(
                color = StudioSurfaceElevated,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x338B5CF6))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = NeonViolet,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WHY THIS MOMENT WILL GO VIRAL",
                            color = NeonViolet,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = clip.whyViralExplanation,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Creator Parallels
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0x33000000), RoundedCornerShape(8.dp))
                            .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💡 Creator Parallel:",
                            color = ViralYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = clip.creatorParallels,
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Platform Suitability Segment Tabs
            PlatformRecommendationTabs(clip = clip)
        }
    }
}

@Composable
private fun MetricProgressBar(
    label: String,
    score: Int,
    icon: ImageVector,
    color: Color
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = label,
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Text(
                text = "$score%",
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = Color(0xFF1E293B)
        )
    }
}

@Composable
private fun PlatformRecommendationTabs(clip: ViralMomentClip) {
    val platforms = listOf("YouTube Shorts", "Instagram Reels", "TikTok")
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.animateContentSize()) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = StudioPrimaryViolet,
            divider = {},
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = StudioPrimaryViolet,
                    height = 2.dp
                )
            }
        ) {
            platforms.forEachIndexed { index, platform ->
                val isSelected = selectedTab == index
                Tab(
                    selected = isSelected,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = platform,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) StudioPrimaryViolet else TextMuted
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val currentPlatform = platforms[selectedTab]
        val reason = clip.platformReasons[currentPlatform]
            ?: "High engagement pacing matches this platform's feed algorithm."

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0x40000000), RoundedCornerShape(10.dp))
                .border(1.dp, StudioOutline, RoundedCornerShape(10.dp))
                .padding(12.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(StudioPrimaryViolet, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Why upload as $currentPlatform:",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reason,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

private fun getScoreTier(score: Int): String {
    return when {
        score >= 95 -> "🚀 Ultra Viral (Explosive Reach)"
        score >= 90 -> "🔥 High Viral Potential (Top 5%)"
        score >= 80 -> "⚡ Strong Growth Candidate"
        score >= 70 -> "📈 Good Engagement Hook"
        else -> "Average Performance"
    }
}
