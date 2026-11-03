package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AspectRatioOption
import com.example.ui.theme.AmbientOrbPurple
import com.example.ui.theme.ApplePurple
import com.example.ui.theme.GlassActiveSpecularBrush
import com.example.ui.theme.GlassSpecularBorderBrush
import com.example.ui.theme.ShadowAmbientGlow
import com.example.ui.theme.ShadowPurpleGlow
import com.example.ui.theme.ShadowSoftBlack
import com.example.ui.theme.StudioPrimaryViolet
import com.example.ui.theme.StudioPrimaryVioletLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.iOSDarkCanvas
import com.example.ui.theme.iOSGlassBorder
import com.example.ui.theme.iOSGlassBorderSubtle
import com.example.ui.theme.iOSGlassFill
import com.example.ui.theme.iOSGlassPill

/**
 * Ambient refractive background representing the smoky glass aesthetic.
 */
@Composable
fun LuminousAtmosphereBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(iOSDarkCanvas)
    ) {
        // Top-Right Subtle Soft-Violet Bloom
        Box(
            modifier = Modifier
                .size(380.dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AmbientOrbPurple, Color.Transparent),
                        radius = 450f
                    )
                )
        )

        // Mid-Left Purple Glow Bloom
        Box(
            modifier = Modifier
                .size(420.dp)
                .align(Alignment.CenterStart)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AmbientOrbPurple, Color.Transparent),
                        radius = 500f
                    )
                )
        )

        content()
    }
}

/**
 * Refined iOS 26/27 Glassmorphic Container Card with soft shadows and specular highlight border.
 */
@Composable
fun IOSGlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(26.dp),
    backgroundColor: Color = iOSGlassFill,
    borderBrush: Brush = GlassSpecularBorderBrush,
    borderWidth: Dp = 1.dp,
    elevation: Dp = 10.dp,
    ambientColor: Color = ShadowSoftBlack,
    spotColor: Color = ShadowPurpleGlow,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = elevation,
                shape = shape,
                ambientColor = ambientColor,
                spotColor = spotColor
            )
            .clip(shape)
            .border(borderWidth, borderBrush, shape),
        color = backgroundColor,
        shape = shape
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            content()
        }
    }
}

/**
 * iOS 27 Tactile Segmented Glass Pill Bar with fluid animated selection.
 */
@Composable
fun IOSGlassSegmentedControl(
    items: List<Pair<String, ImageVector>>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = iOSGlassPill,
        shape = RoundedCornerShape(28.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, GlassSpecularBorderBrush),
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(28.dp), ambientColor = ShadowSoftBlack)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, (label, icon) ->
                val isSelected = selectedIndex == index
                val interactionSource = remember { MutableInteractionSource() }
                val isPressed by interactionSource.collectIsPressedAsState()
                val scale by animateFloatAsState(
                    targetValue = if (isPressed) 0.96f else 1f,
                    animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                    label = "pill_scale"
                )

                val targetBg = if (isSelected) ApplePurple else Color.Transparent
                val animatedBg by animateColorAsState(targetValue = targetBg, label = "bg_color")

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .scale(scale)
                        .clip(RoundedCornerShape(22.dp))
                        .background(animatedBg)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) { onSelectIndex(index) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (isSelected) Color.White else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = label,
                            color = if (isSelected) Color.White else TextSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tactile Aspect Ratio Selection Card with visual aspect wireframe and soft glow.
 */
@Composable
fun IOSAspectSelectionCard(
    option: AspectRatioOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "aspect_scale"
    )

    val shape = RoundedCornerShape(20.dp)
    val borderBrush = if (isSelected) GlassActiveSpecularBrush else GlassSpecularBorderBrush
    val borderWidth = if (isSelected) 1.5.dp else 1.dp
    val bgColor = if (isSelected) Color(0x338B5CF6) else iOSGlassPill

    Surface(
        modifier = modifier
            .scale(scale)
            .shadow(
                elevation = if (isSelected) 12.dp else 4.dp,
                shape = shape,
                ambientColor = ShadowSoftBlack,
                spotColor = if (isSelected) ShadowAmbientGlow else Color.Transparent
            )
            .clip(shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onSelect() }
            .border(borderWidth, borderBrush, shape),
        color = bgColor,
        shape = shape
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Visual Aspect Ratio Frame Wireframe
            Box(
                modifier = Modifier
                    .size(width = 36.dp, height = 36.dp),
                contentAlignment = Alignment.Center
            ) {
                val wireframeModifier = when (option) {
                    AspectRatioOption.RATIO_9_16 -> Modifier.size(width = 18.dp, height = 30.dp)
                    AspectRatioOption.RATIO_1_1 -> Modifier.size(26.dp)
                    AspectRatioOption.RATIO_16_9 -> Modifier.size(width = 32.dp, height = 20.dp)
                }

                Box(
                    modifier = wireframeModifier
                        .clip(RoundedCornerShape(4.dp))
                        .border(
                            1.5.dp,
                            if (isSelected) StudioPrimaryViolet else Color(0x33FFFFFF),
                            RoundedCornerShape(4.dp)
                        )
                        .background(if (isSelected) StudioPrimaryViolet.copy(alpha = 0.2f) else Color(0x11FFFFFF))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = option.label,
                color = if (isSelected) Color.White else TextPrimary,
                fontWeight = FontWeight.Black,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = when (option) {
                    AspectRatioOption.RATIO_9_16 -> "Shorts/Reels"
                    AspectRatioOption.RATIO_1_1 -> "Feed/Square"
                    AspectRatioOption.RATIO_16_9 -> "Standard/TV"
                },
                color = if (isSelected) StudioPrimaryViolet else TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Luminous Pill Action Button with fluid spring feedback and loading state.
 */
@Composable
fun IOSLuminousActionButton(
    text: String,
    icon: ImageVector?,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "btn_scale"
    )

    val shape = RoundedCornerShape(22.dp)

    Box(
        modifier = modifier
            .scale(scale)
            .height(54.dp)
            .shadow(
                elevation = 14.dp,
                shape = shape,
                ambientColor = ShadowSoftBlack,
                spotColor = ShadowPurpleGlow
            )
            .clip(shape)
            .background(
                Brush.horizontalGradient(
                    colors = listOf(StudioPrimaryViolet, Color(0xFF9061F9), StudioPrimaryVioletLight)
                )
            )
            .border(1.dp, Color(0x40FFFFFF), shape)
            .clickable(
                enabled = !isLoading,
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Analyzing Video Moments...",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    letterSpacing = 0.2.sp
                )
            } else {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(19.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = Color.White,
                    fontWeight = FontWeight.Black,
                    fontSize = 15.sp,
                    letterSpacing = 0.3.sp
                )
            }
        }
    }
}
