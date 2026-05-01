package com.monkhex.app.core.designsystem

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MonkHexColors.Elevated.copy(alpha = 0.70f),
                        MonkHexColors.Card.copy(alpha = 0.40f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        MonkHexColors.Cyan.copy(alpha = 0.55f),
                        MonkHexColors.Gold.copy(alpha = 0.35f)
                    )
                ),
                shape = shape
            ),
        content = content
    )
}

fun Modifier.glowBorder(
    shape: Shape = RoundedCornerShape(20.dp),
    color: Color = MonkHexColors.Cyan,
    alpha: Float = 0.35f
): Modifier {
    return this
        .blur(0.4.dp)
        .border(
            width = 1.dp,
            color = color.copy(alpha = alpha),
            shape = shape
        )
}

@Composable
fun GlowBackdrop(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        MonkHexColors.Gold.copy(alpha = 0.18f),
                        Color.Transparent
                    )
                )
            )
    )
}

val RingStroke = Stroke(width = 22f)

