package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ===========================================================================
// Sophisticated Smoky-Glass & Graphite Design System (Virallo)
// ===========================================================================

// Charcoal Background & Graphite Surfaces
val StudioDarkBg = Color(0xFF101010)          // Near-black charcoal foundation
val StudioSurface = Color(0xFF191919)         // Soft graphite surface
val StudioSurfaceSubtle = Color(0xFF202020)   // Faint graphite card surface
val StudioSurfaceElevated = Color(0xFF262626) // Interactive elevated graphite
val StudioSurfaceGlass = Color(0xCC181818)    // Translucent neutral black/gray glass

// Elegant Soft Violet Accents (Strictly No Blue/Cyan)
val StudioPrimaryViolet = Color(0xFFA78BFA)      // Elegant soft violet primary accent
val StudioPrimaryVioletLight = Color(0xFFC4B5FD) // Delicate lavender highlight
val StudioSecondaryAccent = Color(0xFFDDD6FE)    // Secondary soft violet
val StudioSecondaryCyan = Color(0xFFA78BFA)      // Replaced with soft violet (no cyan)
val StudioCyanGlow = Color(0xFFC4B5FD)           // Replaced with lavender (no cyan)

// Semantic Accents (Only when necessary)
val StudioCaptionYellow = Color(0xFFFACC15)   // High-retention yellow
val StudioCaptionRed = Color(0xFFEF4444)      // Warning / urgency red
val StudioGreen = Color(0xFF10B981)           // Success indicator
val StudioMonospaceGreen = Color(0xFF22C55E)  // Monospace status green

// Extremely Subtle Neutral Borders
val StudioBorder = Color(0x1AFFFFFF)          // 10% white subtle neutral border
val StudioBorderSubtle = Color(0x0FFFFFFF)    // 6% white faint line
val StudioBorderActive = Color(0xFFA78BFA)    // Soft violet active border
val StudioBorderCyan = Color(0xFFA78BFA)      // Neutralized to soft violet

// Warm White & Neutral Gray Typography
val TextPrimary = Color(0xFFF5F5F5)           // Warm white headline & body text
val TextSecondary = Color(0xFFA1A1AA)         // Neutral gray secondary text
val TextMuted = Color(0xFF71717A)             // Muted neutral gray captions & labels

// Refined Neutral Specular Highlights
val StudioCardBorderBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0x24FFFFFF),
        Color(0x0DFFFFFF),
        Color(0x04FFFFFF)
    )
)

val StudioActiveBorderBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0x99A78BFA),
        Color(0x33A78BFA)
    )
)

// Ambient Shadows (Neutral / Violet Glow)
val ShadowSoftBlack = Color(0x99000000)
val ShadowAmbientViolet = Color(0x26A78BFA)
val ShadowAmbientCyan = Color(0x26A78BFA)

// ===========================================================================
// Compatibility Aliases for Refined Smoky Glass Architecture
// ===========================================================================
val iOSOledBlack = StudioDarkBg
val iOSDarkCanvas = StudioDarkBg
val iOSOledVoid = StudioDarkBg
val iOSGlassCard = StudioSurface
val iOSGlassFill = StudioSurfaceSubtle
val iOSGlassElevated = StudioSurfaceElevated
val iOSGlassPill = StudioSurfaceSubtle
val iOSGlassInputBg = Color(0xFF141414)
val iOSGlassBorder = StudioBorder
val iOSGlassBorderSubtle = StudioBorderSubtle
val iOSGlassBorderActive = StudioPrimaryViolet
val iOSGlassHighlight = Color(0x18FFFFFF)

val GlassSpecularBorderBrush = StudioCardBorderBrush
val GlassActiveSpecularBrush = StudioActiveBorderBrush
val GlassPillSpecularBrush = StudioCardBorderBrush

val ApplePurple = StudioPrimaryViolet
val ApplePurpleGlow = StudioPrimaryVioletLight
val AppleCyan = StudioPrimaryViolet           // Strictly purged cyan -> Soft Violet
val AppleCyanGlow = StudioPrimaryVioletLight
val AppleRed = StudioCaptionRed
val AppleYellow = StudioCaptionYellow
val AppleGreen = StudioGreen

val ShadowPurpleGlow = ShadowAmbientViolet
val ShadowAmbientGlow = ShadowAmbientViolet
val AmbientOrbCyan = Color(0x08A78BFA)        // Purged cyan orb -> subtle violet glow
val AmbientOrbPurple = Color(0x0CA78BFA)

val NeonCyan = StudioPrimaryViolet            // Strictly purged cyan -> Soft Violet
val NeonViolet = StudioPrimaryViolet
val ObsidianBg = StudioDarkBg
val StudioCardBg = StudioSurface
val StudioOutline = StudioBorder
val ViralFlame = StudioCaptionRed
val ViralGreen = StudioGreen
val ViralYellow = StudioCaptionYellow

val LightBg = Color(0xFFF5F5F5)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceElevated = Color(0xFFEEEEEE)
val LightOutline = Color(0xFFE4E4E7)
