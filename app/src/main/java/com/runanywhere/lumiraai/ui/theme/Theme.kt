package com.runanywhere.lumiraai.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RuralCareLightColorScheme = lightColorScheme(
    primary = Color(0xFF03A9F4), // SkyBlue
    onPrimary = Color(0xFFFFFFFF), // WellnessWhite
    primaryContainer = Color(0xFF87CEEB), // SkyBlueLight
    onPrimaryContainer = Color(0xFF333333), // DarkGray

    secondary = Color(0xFF8BC34A), // HealthyGreen
    onSecondary = Color(0xFFFFFFFF), // WellnessWhite
    secondaryContainer = Color(0xFFC6F4D6), // HealthyGreenLight
    onSecondaryContainer = Color(0xFF333333), // DarkGray

    tertiary = Color(0xFFFF9800), // AccentOrange
    onTertiary = Color(0xFFFFFFFF), // WellnessWhite
    tertiaryContainer = Color(0xFFFFC107), // SunsetOrange
    onTertiaryContainer = Color(0xFF333333), // DarkGray

    error = Color(0xFFB00020), // ErrorRed
    onError = Color(0xFFFFFFFF), // WellnessWhite
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    background = Color(0xFFFFFFFF), // WellnessWhite
    onBackground = Color(0xFF333333), // DarkGray
    surface = Color(0xFFFFFFFF), // WellnessWhite
    onSurface = Color(0xFF333333), // DarkGray
    surfaceVariant = Color(0xFFE5E5EA), // SoftGray
    onSurfaceVariant = Color(0xFF666666), // MediumGray

    outline = Color(0xFF666666), // MediumGray
    outlineVariant = Color(0xFFE0E0E0),
    scrim = Color(0x80000000),

    inverseSurface = Color(0xFF333333), // DarkGray
    inverseOnSurface = Color(0xFFFFFFFF), // WellnessWhite
    inversePrimary = Color(0xFF87CEEB), // SkyBlueLight
)

private val RuralCareDarkColorScheme = darkColorScheme(
    primary = Color(0xFF87CEEB), // SkyBlueLight
    onPrimary = Color(0xFF333333), // DarkGray
    primaryContainer = Color(0xFF4682B4), // SkyBlueDark
    onPrimaryContainer = Color(0xFFFFFFFF), // WellnessWhite

    secondary = Color(0xFFC6F4D6), // HealthyGreenLight
    onSecondary = Color(0xFF333333), // DarkGray
    secondaryContainer = Color(0xFF3E8E41), // HealthyGreenDark
    onSecondaryContainer = Color(0xFFFFFFFF), // WellnessWhite

    tertiary = Color(0xFFFF9800), // AccentOrange
    onTertiary = Color(0xFF333333), // DarkGray
    tertiaryContainer = Color(0xFFE65100),
    onTertiaryContainer = Color(0xFFFFFFFF), // WellnessWhite

    error = Color(0xFFB00020), // ErrorRed
    onError = Color(0xFF333333), // DarkGray
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454F),
    scrim = Color(0xFF000000),

    inverseSurface = Color(0xFFE6E1E5),
    inverseOnSurface = Color(0xFF313033),
    inversePrimary = Color(0xFF03A9F4), // SkyBlue
)

@Composable
fun LumiraAITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> RuralCareDarkColorScheme
        else -> RuralCareLightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}