package com.paint.randompeoplek.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = lime500,
    onSurfaceVariant = Color.White,
    outline = Color.White,
    secondary = lime700,
    background = white800,
    surface = Color.White.copy(alpha = .85f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = white800,
    onSurface = gray900.copy(alpha = 0.8f)
)

private val DarkColorScheme = darkColorScheme(
    primary = lime700,
    onSurfaceVariant = Color.White,
    outline = Color.White,
    secondary = lime900,
    background = gray900,
    surface = Color.White.copy(alpha = 0.15f),
    onPrimary = gray900,
    onSecondary = gray900,
    onBackground = white800,
    onSurface = Color.White.copy(alpha = .8f)
)

@Composable
fun RandomPeopleKTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
