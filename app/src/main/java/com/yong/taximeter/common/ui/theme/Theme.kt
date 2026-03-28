package com.yong.taximeter.common.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark Color Scheme
private val DarkColorScheme = darkColorScheme(
    primary = Blue80,
    surface = Grey10,
    onSurface = Grey90,
)

// Light Color Scheme
private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    surface = White,
    onSurface = Grey10,
)

@Composable
fun TaxiMeterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // App UI Color Scheme
    // - Depends on dynamic color system
    val appColorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Meter Color Scheme
    val meterColorScheme = getMeterColors(isDark = darkTheme)

    // Set status bar colors by dark mode flag
    val view = LocalView.current
    if(!view.isInEditMode) {
        SideEffect {
            // Get window controller from current view
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            // Set status bar, navigation bar color
            insetsController.isAppearanceLightStatusBars = darkTheme.not()
            insetsController.isAppearanceLightNavigationBars = darkTheme.not()
        }
    }

    // Default App Theme, provides meter color scheme
    CompositionLocalProvider(
        LocalMeterColors provides meterColorScheme
    ) {
        MaterialTheme(
            colorScheme = appColorScheme,
            typography = Typography,
            content = content,
        )
    }
}

/**
 * Meter Color Provider
 */
val LocalMeterColors = staticCompositionLocalOf { getMeterColors(isDark = false) }

/**
 * Color presets for Meter UI
 */
@Immutable
data class MeterColors(
    val buttonText: Color,
    val blue: Color,
    val green: Color,
    val mint: Color,
    val red: Color,
    val yellow: Color,
    val background: Color,
    val onBackground: Color,
)

/**
 * Get [MeterColors] instance with [isDark] flag
 */
private fun getMeterColors(
    isDark: Boolean,
): MeterColors = if(isDark) {
    MeterColors(
        buttonText = MeterButtonTextDark,
        blue = MeterBlueDark,
        green = MeterGreenDark,
        mint = MeterMint,
        red = MeterRedDark,
        yellow = MeterYellowDark,
        background = MeterBackgroundDark,
        onBackground = MeterTextPrimaryDark,
    )
} else {
    MeterColors(
        buttonText = MeterButtonTextLight,
        blue = MeterBlueLight,
        green = MeterGreenLight,
        mint = MeterMint,
        red = MeterRedLight,
        yellow = MeterYellowLight,
        background = MeterBackgroundLight,
        onBackground = MeterTextPrimaryLight,
    )
}

/**
 * Color instance for get each color easily
 * - Usage example: `MeterTheme.colors.blue`
 */
object MeterTheme {
    val colors: MeterColors
        @Composable
        get() = LocalMeterColors.current
}