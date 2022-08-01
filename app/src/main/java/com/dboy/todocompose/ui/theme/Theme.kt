package com.dboy.todocompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = lightGray,
    primaryVariant = AnotherBlack,
    secondary = SonicSilver
)

private val LightColorPalette = lightColors(
    primary = Indigo,
    primaryVariant = IndigoOutro,
    secondary = IndigoOutro

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun ToDoComposeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        rememberSystemUiController().setSystemBarsColor(
            color = Color.Black
        )
        DarkColorPalette
    } else {
        rememberSystemUiController().setSystemBarsColor(
            color = IndigoDark
        )
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}