package com.metroplus.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.metroplus.app.R

// PlusJakartaSans — trial app ka font, poori native app me yahi chalega.
val AppFontFamily = FontFamily(Font(R.font.plusjakarta))

// Har Material style me yahi family, taake koi Text default font pe na jaye.
val AppTypography = Typography(
    displayLarge = TextStyle(fontFamily = AppFontFamily),
    displayMedium = TextStyle(fontFamily = AppFontFamily),
    displaySmall = TextStyle(fontFamily = AppFontFamily),
    headlineLarge = TextStyle(fontFamily = AppFontFamily),
    headlineMedium = TextStyle(fontFamily = AppFontFamily),
    headlineSmall = TextStyle(fontFamily = AppFontFamily),
    titleLarge = TextStyle(fontFamily = AppFontFamily),
    titleMedium = TextStyle(fontFamily = AppFontFamily),
    titleSmall = TextStyle(fontFamily = AppFontFamily),
    bodyLarge = TextStyle(fontFamily = AppFontFamily),
    bodyMedium = TextStyle(fontFamily = AppFontFamily),
    bodySmall = TextStyle(fontFamily = AppFontFamily),
    labelLarge = TextStyle(fontFamily = AppFontFamily),
    labelMedium = TextStyle(fontFamily = AppFontFamily),
    labelSmall = TextStyle(fontFamily = AppFontFamily)
)
