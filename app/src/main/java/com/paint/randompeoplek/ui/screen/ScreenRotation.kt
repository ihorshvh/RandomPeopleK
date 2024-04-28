package com.paint.randompeoplek.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberScreenInfo(): ScreenInfo {
    val configuration = LocalConfiguration.current
    return ScreenInfo(
        screenOrientation = if (configuration.screenHeightDp > configuration.screenWidthDp) {
            ScreenInfo.ScreenOrientation.Portrait
        } else {
            ScreenInfo.ScreenOrientation.Landscape
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}

data class ScreenInfo(
    val screenOrientation: ScreenOrientation,
    val screenWidth: Dp,
    val screenHeight: Dp
) {
    sealed class ScreenOrientation {
        data object Portrait : ScreenOrientation()
        data object Landscape : ScreenOrientation()
    }
}