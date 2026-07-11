package com.yong.taximeter.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.yong.taximeter.common.ui.theme.TaxiMeterTheme
import com.yong.taximeter.domain.model.ThemeModeSetting
import com.yong.taximeter.domain.repository.SettingRepository
import com.yong.taximeter.navigation.TaxiMeterNavHost
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    @Inject
    lateinit var settingRepository: SettingRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val themeMode by settingRepository.observeThemeMode()
                .collectAsStateWithLifecycle(initialValue = ThemeModeSetting.SYSTEM)

            val isDarkTheme = when (themeMode) {
                ThemeModeSetting.SYSTEM -> isSystemInDarkTheme()
                ThemeModeSetting.DARK -> true
                ThemeModeSetting.LIGHT -> false
            }

            TaxiMeterTheme(darkTheme = isDarkTheme) {
                val navController = rememberNavController()

                // TaxiMeter Nav Host
                TaxiMeterNavHost(
                    modifier = Modifier
                        .fillMaxSize(),
                    navController = navController,
                )
            }
        }
    }
}