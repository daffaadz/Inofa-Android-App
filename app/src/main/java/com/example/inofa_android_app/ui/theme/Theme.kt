package com.example.inofa_android_app.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// ----- Light Theme -----
val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = White,

    secondary = Secondary,
    onSecondary = Black,

    background = BackgroundLight,
    onBackground = Black,

    surface = White,
    onSurface = Black,

    tertiary = Primary20,
)

// ----- Dark Theme -----
val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = Black,

    secondary = Secondary,
    onSecondary = Black,

    background = Color(0xFF121212),
    onBackground = White,

    surface = Color(0xFF1E1E1E),
    onSurface = White,

    tertiary = Primary10,
)


@Composable
fun InofaAndroidAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    val context = LocalContext.current

    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                if (darkTheme) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            }
            darkTheme -> DarkColorScheme
            else -> LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}