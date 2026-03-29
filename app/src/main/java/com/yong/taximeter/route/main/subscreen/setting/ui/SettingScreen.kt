package com.yong.taximeter.route.main.subscreen.setting.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yong.taximeter.route.main.subscreen.setting.viewmodel.SettingViewModel

/**
 * Setting Screen
 */
@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier,
    ) {
        Text("Setting Screen")
    }
}