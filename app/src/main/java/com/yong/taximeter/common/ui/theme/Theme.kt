package com.yong.taximeter.common.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

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
    // Dynamic color is available on Android 12+
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
        typography = Typography,
        content = content
    )
}

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
        blue = MeterBlueDark,
        green = MeterGreenDark,
        mint = MeterMint,
        red = MeterRedDark,
        yellow = MeterYellowDark,
        background = MeterBackgroundLight,
        onBackground = MeterTextPrimaryLight,
    )
}