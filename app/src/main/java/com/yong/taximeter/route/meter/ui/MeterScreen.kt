package com.yong.taximeter.route.meter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yong.taximeter.route.meter.viewmodel.MeterViewModel

/**
 * Meter Screen
 */
@Composable
fun MeterScreen(
    modifier: Modifier = Modifier,
    viewModel: MeterViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier,
    ) {
        Text("Meter Screen")
    }
}