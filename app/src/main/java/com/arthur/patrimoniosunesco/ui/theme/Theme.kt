package com.arthur.patrimoniosunesco.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF176B87),
    onPrimary = Color.White,
    secondary = Color(0xFFB87938),
    background = Color(0xFFF7F4EE),
    surface = Color(0xFFFFFBF5),
    onSurface = Color(0xFF202326),
    onBackground = Color(0xFF202326)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF75CBE8),
    secondary = Color(0xFFF0B878),
    background = Color(0xFF11191D),
    surface = Color(0xFF19252A),
    onSurface = Color(0xFFE8F1F3),
    onBackground = Color(0xFFE8F1F3)
)

@Composable
fun PatrimoniosUNESCTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
