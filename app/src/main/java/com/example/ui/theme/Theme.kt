package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TeaGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = TeaGreenContainer,
    onPrimaryContainer = TeaGreenDark,
    secondary = TeaGold,
    onSecondary = Color.White,
    secondaryContainer = TeaGoldContainer,
    onSecondaryContainer = Color(0xFF4A3408),
    tertiary = TeaAmber,
    onTertiary = Color.White,
    background = TeaCreamBg,
    onBackground = TeaTextPrimary,
    surface = TeaSurface,
    onSurface = TeaTextPrimary,
    surfaceVariant = TeaSurfaceVariant,
    onSurfaceVariant = TeaTextSecondary,
    outline = TeaBorder,
    outlineVariant = Color(0xFFE8E2D8),
)

private val DarkColorScheme = darkColorScheme(
    primary = TeaGoldLight,
    onPrimary = TeaGreenDark,
    primaryContainer = TeaGreenLight,
    onPrimaryContainer = Color.White,
    secondary = TeaGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF3B2B0A),
    onSecondaryContainer = TeaGoldLight,
    tertiary = Color(0xFFF59E0B),
    onTertiary = Color.Black,
    background = Color(0xFF0F1613),
    onBackground = Color(0xFFF1F5F3),
    surface = Color(0xFF18221D),
    onSurface = Color(0xFFF1F5F3),
    surfaceVariant = Color(0xFF222F29),
    onSurfaceVariant = Color(0xFFB0BEB8),
    outline = Color(0xFF35443D),
    outlineVariant = Color(0xFF26332C),
)

@Composable
fun SktTeaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    SktTeaTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
