package com.monkhex.app.core.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object MonkHexColors {
    val Background = Color(0xFF0B0F1A)
    val Card = Color(0xFF121826)
    val Elevated = Color(0xFF1A2233)
    val Gold = Color(0xFFF5B301)
    val Cyan = Color(0xFF00D1FF)
    val TextPrimary = Color(0xFFF4F6FA)
    val TextSecondary = Color(0xFF98A2B3)
}

private val MonkHexDarkColorScheme = darkColorScheme(
    primary = MonkHexColors.Gold,
    secondary = MonkHexColors.Cyan,
    background = MonkHexColors.Background,
    surface = MonkHexColors.Card,
    onPrimary = MonkHexColors.Background,
    onSecondary = MonkHexColors.Background,
    onBackground = MonkHexColors.TextPrimary,
    onSurface = MonkHexColors.TextPrimary
)

private val MonkHexTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 42.sp,
        letterSpacing = 0.5.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 34.sp,
        letterSpacing = 0.4.sp
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    )
)

@Composable
fun MonkHexTheme(content: @Composable () -> Unit) {
    if (!isSystemInDarkTheme()) {
        // MonkHex ships as dark-first for visual identity and discipline focus.
    }
    MaterialTheme(
        colorScheme = MonkHexDarkColorScheme,
        typography = MonkHexTypography,
        content = content
    )
}

