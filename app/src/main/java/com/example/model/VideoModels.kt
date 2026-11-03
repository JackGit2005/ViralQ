package com.example.model

enum class AspectRatioOption(
    val label: String,
    val ratioValue: Float,
    val description: String,
    val platformTarget: String
) {
    RATIO_9_16("9:16", 9f / 16f, "Vertical Fullscreen", "YouTube Shorts, TikTok, Instagram Reels"),
    RATIO_1_1("1:1", 1f, "Square Canvas", "Instagram Feed, LinkedIn, Threads"),
    RATIO_16_9("16:9", 16f / 9f, "Widescreen Cinema", "YouTube Landscape, X / Twitter, Facebook")
}

enum class FramingMode(val displayName: String, val subtitle: String) {
    SMART_SPEAKER_TRACK("AI Face & Speaker Track", "Auto-centers active speaker in 9:16 vertical"),
    SPLIT_SCREEN_STACKED("Multi-Speaker Split", "Stacked top/bottom layout for podcasts & debates"),
    DYNAMIC_MOTION_PAN("Action Motion Pan", "Follows rapid movements and high-energy gestures")
}

enum class CaptionStyleType(
    val displayName: String,
    val subtitle: String,
    val vibeTag: String
) {
    PUNCHY_POP("Punchy Pop", "Bold typography, yellow/cyan highlights & dark shadow", "🔥 High Retention"),
    KARAOKE_NEON("Karaoke Neon", "Word-by-word highlighting with magenta & cyan glow", "⚡ Dynamic Flow"),
    MINIMAL_CLEAN("Minimal Clean", "Crisp white typography, subtle translucent background", "✨ Clean Studio"),
    BEAST_POP("Beast Pop", "High-contrast red & yellow, playful accents", "🚀 Viral Impact"),
    CYBER_PHOSPHOR("Cyber Phosphor", "Retro green tech aesthetic, monospace", "👾 Tech / Monospace"),

    // Aliases for backward compatibility
    HORMOZI_YELLOW("Punchy Pop (Hormozi)", "Bold typography, yellow highlights & dark shadow", "🔥 High Retention"),
    CYBER_GREEN("Cyber Phosphor", "Retro terminal phosphor green monospace", "👾 Tech / Gaming")
}

enum class CaptionPosition(val label: String) {
    TOP("Top"),
    CENTER("Center"),
    BOTTOM("Bottom")
}

data class CaptionLine(
    val startSeconds: Float,
    val endSeconds: Float,
    val text: String,
    val highlightedWord: String = ""
)

data class CaptionOption(
    val category: String,
    val captionText: String,
    val impactAnalysis: String,
    val designTip: String
)

data class PlatformSegmentLink(
    val platformName: String,
    val iconName: String,
    val url: String,
    val label: String,
    val description: String
)

data class ThumbnailVariant(
    val id: String,
    val label: String,
    val overlayText: String,
    val headlineColor: Long,
    val predictedCtr: String,
    val hookScore: Int,
    val rationale: String
)

data class SocialSchedulePlan(
    val platform: String,
    val optimalTimeWindow: String,
    val peakActivityDay: String,
    val algorithmRationale: String,
    var isQueued: Boolean = false
)

data class RetentionDiagnostic(
    val retentionCurve: List<Int>, // 0-100% curve points across the clip
    val highSwipeRiskSecond: String, // e.g. "Second 0:07 - Low Audio Energy"
    val autoFixApplied: String, // e.g. "Injected 1.3x Kinetic Punch-In Zoom + Swoosh SFX"
    val predictedAvgViewDurationPct: Int // e.g. 124%
)

data class HookABVariation(
    val variantId: String,
    val title: String, // Variation A (Direct Start), Variation B (Question Hook), Variation C (Reverse Climax)
    val onScreenText: String,
    val first3SecondsAction: String,
    val predictedRetentionBonus: String
)

data class CameraAndSafetyStrategy(
    val layoutStrategy: String, // "Single Speaker Dynamic Zoom", "Multi-Speaker Split Screen"
    val zoomTriggers: List<String>,
    val safetyStatus: String // "Clean (100% Monetization Friendly)" or "Auto-Bleeped & Masked at 0:04"
)

data class SoundVibeMatch(
    val sentiment: String,
    val audioVibe: String,
    val sfxTriggers: List<String>
)

data class ViralMetrics(
    val overallScore: Int, // 0 - 100 calculated from weighted factors
    val hookScore: Int, // Hook strength (30% weight)
    val emotionalIntensity: Int, // Emotional intensity (25% weight)
    val narrativePayoff: Int = 85, // Narrative payoff (20% weight)
    val standaloneContext: Int = 80, // Standalone context (15% weight)
    val shareabilityScore: Int = 88, // Shareability (10% weight)
    val retentionScore: Int = 86 // Retention baseline
) {
    companion object {
        fun calculateWeightedScore(
            hook: Int,
            emotion: Int,
            payoff: Int,
            context: Int,
            shareability: Int
        ): Int {
            val weighted = (hook * 0.30f) +
                (emotion * 0.25f) +
                (payoff * 0.20f) +
                (context * 0.15f) +
                (shareability * 0.10f)
            return weighted.toInt().coerceIn(1, 100)
        }
    }
}

data class ViralMomentClip(
    val id: String,
    val title: String,
    val startSeconds: Int,
    val endSeconds: Int,
    val durationSeconds: Int = (endSeconds - startSeconds).coerceAtLeast(1),
    val timeRangeFormatted: String,
    val metrics: ViralMetrics,
    val whyViralExplanation: String,
    val creatorParallels: String,
    val bestPlatform: String,
    val platformReasons: Map<String, String>,
    val platformLinks: List<PlatformSegmentLink>,
    val captions: List<CaptionLine>,
    val captionOptions: List<CaptionOption>,
    val thumbnailVariants: List<ThumbnailVariant> = emptyList(),
    val schedulePlans: List<SocialSchedulePlan> = emptyList(),
    val retentionDiagnostic: RetentionDiagnostic,
    val hookVariations: List<HookABVariation>,
    val cameraStrategy: CameraAndSafetyStrategy,
    val soundVibeMatch: SoundVibeMatch,
    val suggestedTitles: List<String>,
    val tags: List<String>,
    val isBookmarked: Boolean = false
)

data class VideoAnalysisResult(
    val url: String,
    val videoId: String,
    val title: String,
    val channel: String,
    val durationFormatted: String,
    val thumbnailUrl: String,
    val selectedFormat: AspectRatioOption,
    val clips: List<ViralMomentClip>,
    val analyzedAtMillis: Long = System.currentTimeMillis()
)
