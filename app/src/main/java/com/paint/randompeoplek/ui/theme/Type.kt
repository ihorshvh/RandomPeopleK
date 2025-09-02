package com.paint.randompeoplek.ui.theme

import androidx.compose.material3.Typography // Changed import
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.paint.randompeoplek.R

private val fontFamilyKulim = FontFamily(
    listOf(
        Font(
            resId = R.font.kulim_park_regular
        ),
        Font(
            resId = R.font.kulim_park_light,
            weight = FontWeight.Light
        )
    )
)

private val fontFamilyLato = FontFamily(
    listOf(
        Font(
            resId = R.font.lato_regular
        ),
        Font(
            resId = R.font.lato_bold,
            weight = FontWeight.Bold
        )
    )
)

// Define M3 text styles, mapping from your existing ones
val typography = Typography(
    // defaultFontFamily = fontFamilyLato, // You can set a default, or apply specific fonts below

    // Mapping h1 to titleLarge (example, adjust as needed)
    titleLarge = TextStyle(
        fontFamily = fontFamilyKulim,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = (1.15).sp
    ),
    // Mapping h2 to titleMedium
    titleMedium = TextStyle(
        fontFamily = fontFamilyKulim,
        fontSize = 16.sp,
        letterSpacing = (1.15).sp
    ),
    // Mapping h3 to titleSmall
    titleSmall = TextStyle(
        fontFamily = fontFamilyLato, // Assuming Lato for this based on your previous default
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    // Mapping body1 to bodyLarge
    bodyLarge = TextStyle(
        fontFamily = fontFamilyLato,
        fontSize = 14.sp,
        letterSpacing = 0.sp
    ),
    // Mapping button to labelLarge
    labelLarge = TextStyle(
        fontFamily = fontFamilyLato,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = (1.15).sp
    ),
    // Mapping caption to bodySmall or labelSmall
    bodySmall = TextStyle(
        fontFamily = fontFamilyKulim,
        fontSize = 12.sp,
        letterSpacing = (1.15).sp
    ),
    // Add other M3 styles if needed, or leave them to M3 defaults
    // Example of using another style:
    // headlineSmall = TextStyle(
    //    fontFamily = fontFamilyKulim,
    //    fontWeight = FontWeight.SemiBold,
    //    fontSize = 22.sp
    // )
)
