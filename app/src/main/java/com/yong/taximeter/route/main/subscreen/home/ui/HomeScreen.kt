package com.yong.taximeter.route.main.subscreen.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yong.taximeter.route.main.subscreen.home.viewmodel.HomeViewModel

/**
 * Home Screen
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToMeter: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Text("Home Screen")
    }
}