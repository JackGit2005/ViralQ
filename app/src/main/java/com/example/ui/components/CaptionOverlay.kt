package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CaptionLine
import com.example.model.CaptionPosition
import com.example.model.CaptionStyleType

@Composable
fun BoxScope.CaptionOverlay(
    captions: List<CaptionLine>,
    currentTimeSeconds: Float,
    styleType: CaptionStyleType,
    position: CaptionPosition,
    fontSizeSp: Float
) {
    // Find active caption line based on current play time
    val activeLine = captions.firstOrNull {
        currentTimeSeconds >= it.startSeconds && currentTimeSeconds <= it.endSeconds
    } ?: captions.firstOrNull()

    val alignment = when (position) {
        CaptionPosition.TOP -> Alignment.TopCenter
        CaptionPosition.CENTER -> Alignment.Center
        CaptionPosition.BOTTOM -> Alignment.BottomCenter
    }

    val verticalPadding = when (position) {
        CaptionPosition.TOP -> 36.dp
        CaptionPosition.CENTER -> 8.dp
        CaptionPosition.BOTTOM -> 54.dp // Higher padding to clear TikTok/Reels UI
    }

    AnimatedVisibility(
        visible = activeLine != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = Modifier
            .align(alignment)
            .padding(horizontal = 16.dp, vertical = verticalPadding)
    ) {
        if (activeLine != null) {
            when (styleType) {
                CaptionStyleType.PUNCHY_POP, CaptionStyleType.HORMOZI_YELLOW -> {
                    HormoziCaption(line = activeLine, fontSizeSp = fontSizeSp)
                }
                CaptionStyleType.KARAOKE_NEON -> {
                    KaraokeNeonCaption(line = activeLine, fontSizeSp = fontSizeSp)
                }
                CaptionStyleType.MINIMAL_CLEAN -> {
                    MinimalCleanCaption(line = activeLine, fontSizeSp = fontSizeSp)
                }
                CaptionStyleType.BEAST_POP -> {
                    BeastPopCaption(line = activeLine, fontSizeSp = fontSizeSp)
                }
                CaptionStyleType.CYBER_PHOSPHOR, CaptionStyleType.CYBER_GREEN -> {
                    CyberGreenCaption(line = activeLine, fontSizeSp = fontSizeSp)
                }
            }
        }
    }
}

@Composable
private fun HormoziCaption(line: CaptionLine, fontSizeSp: Float) {
    val words = line.text.split(" ")
    val focusWord = line.highlightedWord.ifEmpty { words.lastOrNull() ?: "" }

    val annotated = buildAnnotatedString {
        words.forEachIndexed { index, word ->
            val isHighlighted = word.contains(focusWord, ignoreCase = true)
            if (isHighlighted) {
                withStyle(
                    SpanStyle(
                        color = Color(0xFFFACC15), // Neon Yellow
                        fontWeight = FontWeight.Black,
                        shadow = Shadow(
                            color = Color(0xFFF59E0B),
                            offset = Offset(0f, 0f),
                            blurRadius = 16f
                        )
                    )
                ) {
                    append(word.uppercase())
                }
            } else {
                withStyle(
                    SpanStyle(
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                ) {
                    append(word.uppercase())
                }
            }
            if (index < words.size - 1) append(" ")
        }
    }

    Box(
        modifier = Modifier
            .background(Color(0xB3000000), RoundedCornerShape(8.dp))
            .border(2.dp, Color(0x66FACC15), RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = annotated,
            fontSize = fontSizeSp.sp,
            textAlign = TextAlign.Center,
            lineHeight = (fontSizeSp * 1.25f).sp
        )
    }
}

@Composable
private fun KaraokeNeonCaption(line: CaptionLine, fontSizeSp: Float) {
    val words = line.text.split(" ")
    val focusWord = line.highlightedWord.ifEmpty { words.firstOrNull() ?: "" }

    val annotated = buildAnnotatedString {
        words.forEachIndexed { index, word ->
            val isHighlighted = word.contains(focusWord, ignoreCase = true)
            if (isHighlighted) {
                withStyle(
                    SpanStyle(
                        color = Color(0xFFA78BFA), // Soft Violet Accent
                        fontWeight = FontWeight.Black,
                        shadow = Shadow(
                            color = Color(0xFF8B5CF6),
                            offset = Offset(0f, 0f),
                            blurRadius = 24f
                        )
                    )
                ) {
                    append(word)
                }
            } else {
                withStyle(
                    SpanStyle(
                        color = Color(0xFFFF2A85), // Neon Magenta
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = Color(0xFFBE185D),
                            offset = Offset(2f, 2f),
                            blurRadius = 12f
                        )
                    )
                ) {
                    append(word)
                }
            }
            if (index < words.size - 1) append(" ")
        }
    }

    Box(
        modifier = Modifier
            .background(Color(0xCC0D0221), RoundedCornerShape(12.dp))
            .border(1.5.dp, Color(0xFF00F0FF), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = annotated,
            fontSize = fontSizeSp.sp,
            textAlign = TextAlign.Center,
            lineHeight = (fontSizeSp * 1.3f).sp
        )
    }
}

@Composable
private fun MinimalCleanCaption(line: CaptionLine, fontSizeSp: Float) {
    Box(
        modifier = Modifier
            .background(Color(0xD91E293B), RoundedCornerShape(24.dp))
            .border(1.dp, Color(0x40FFFFFF), RoundedCornerShape(24.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text(
            text = line.text,
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = fontSizeSp.sp,
            textAlign = TextAlign.Center,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
private fun BeastPopCaption(line: CaptionLine, fontSizeSp: Float) {
    Box(
        modifier = Modifier
            .background(Color(0xFFDC2626), RoundedCornerShape(10.dp))
            .border(3.dp, Color(0xFFFEF08A), RoundedCornerShape(10.dp))
            .shadow(12.dp, RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "🔥 ${line.text.uppercase()} 🚀",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = fontSizeSp.sp,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    shadow = Shadow(Color.Black, Offset(3f, 3f), 4f)
                )
            )
        }
    }
}

@Composable
private fun CyberGreenCaption(line: CaptionLine, fontSizeSp: Float) {
    Box(
        modifier = Modifier
            .background(Color(0xEE051A0E), RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFF10B981), RoundedCornerShape(4.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = "> ${line.text} _",
            color = Color(0xFF34D399),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = fontSizeSp.sp,
            textAlign = TextAlign.Center,
            style = TextStyle(
                shadow = Shadow(Color(0xFF059669), Offset(0f, 0f), 10f)
            )
        )
    }
}
