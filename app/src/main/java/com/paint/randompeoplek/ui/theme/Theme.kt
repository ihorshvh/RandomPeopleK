package com.paint.randompeoplek.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = lime500,
    secondary = lime700,
    background = white800,
    surface = Color.White.copy(alpha = .85f),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = white800,
    onSurface = gray900.copy(alpha = 0.8f),
    tertiary = lime500,
    surfaceVariant = Color.White,
    onSurfaceVariant = gray900,
    outline = gray900
)

private val DarkColorScheme = darkColorScheme(
    primary = lime700,
    secondary = lime900,
    background = gray900,
    surface = Color.White.copy(alpha = 0.15f),
    onPrimary = gray900,
    onSecondary = gray900,
    onBackground = white800,
    onSurface = Color.White.copy(alpha = .8f),
    tertiary = lime700,
    surfaceVariant = gray900,
    onSurfaceVariant = Color.White,
    outline = gray900
)

@Composable
fun RandomPeopleKTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        shapes = shapes,
        content = content
    )
}
