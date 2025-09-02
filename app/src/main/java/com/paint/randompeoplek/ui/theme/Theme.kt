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
    onBackground = white800, // M3 uses onBackground for text/icons on background
    onSurface = gray900.copy(alpha = 0.8f), // M3 uses onSurface for text/icons on surface
    // Add other M3 colors as needed, mapping from your M2 palette
//    tertiary = limeA200, // Example, adjust as needed
//    surfaceVariant = white900, // Example, adjust as needed
//    onSurfaceVariant = gray900, // Example, adjust as needed
//    outline = gray500 // Example for borders/dividers
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
//    tertiary = limeA400, // Example
//    surfaceVariant = gray800, // Example
//    onSurfaceVariant = white900, // Example
//    outline = gray600 // Example
)

@Composable
fun RandomPeopleKTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android S+
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
        typography = typography, // Assuming 'typography' is M3 compatible or will be updated
        shapes = shapes, // Assuming 'shapes' are M3 compatible or will be updated
        content = content
    )
}
