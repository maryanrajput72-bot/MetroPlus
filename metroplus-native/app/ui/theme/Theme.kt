package com.metroplus.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MetroPlusColorScheme = lightColorScheme(
    primary = MetroCoral,
    onPrimary = Color.White,
    secondary = MetroCoralDark,
    background = HomeBackground,
    surface = Color.White,
    onBackground = Ink,
    onSurface = Ink
)

@Composable
fun MetroPlusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MetroPlusColorScheme,
        typography = AppTypography,
        content = content
    )
}
