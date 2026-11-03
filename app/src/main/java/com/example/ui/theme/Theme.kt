package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = StudioPrimaryViolet,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF2E2442),
    onPrimaryContainer = Color(0xFFDDD6FE),
    secondary = StudioSecondaryAccent,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF262626),
    onSecondaryContainer = Color(0xFFF5F5F5),
    tertiary = StudioCaptionYellow,
    onTertiary = Color.Black,
    error = StudioCaptionRed,
    background = StudioDarkBg,
    onBackground = TextPrimary,
    surface = StudioSurface,
    onSurface = TextPrimary,
    surfaceVariant = StudioSurfaceSubtle,
    onSurfaceVariant = TextSecondary,
    outline = StudioBorder
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF7C3AED),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = Color(0xFF4C1D95),
    secondary = Color(0xFF8B5CF6),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF4F4F5),
    onSecondaryContainer = Color(0xFF18181B),
    tertiary = Color(0xFFD97706),
    onTertiary = Color.White,
    error = StudioCaptionRed,
    background = LightBg,
    onBackground = Color(0xFF18181B),
    surface = LightSurface,
    onSurface = Color(0xFF18181B),
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = Color(0xFF71717A),
    outline = LightOutline
)

@Composable
fun ViralloTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun ViralIQTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    ViralloTheme(darkTheme = darkTheme, content = content)
}
