package br.com.mrocigno.horizonlivemap.core.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = dark,
    primaryVariant = dark,
    secondary = light,
    background = dark,
    surface = dark,
    onBackground = light,
    onSurface = light,
    onPrimary = light,
    onError = light,
    onSecondary = light,
)

private val LightColorPalette = lightColors(
    primary = dark,
    primaryVariant = dark,
    secondary = light,
    background = light,
    surface = light,
    onBackground = Color.Black,
    onSurface = Color.Black,
    onPrimary = Color.Black,
    onError = Color.Black,
    onSecondary = Color.Black,
)

@Composable
fun HorizonLiveMapTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}