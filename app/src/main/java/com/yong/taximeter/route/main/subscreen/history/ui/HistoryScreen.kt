package com.yong.taximeter.route.main.subscreen.history.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yong.taximeter.route.main.subscreen.history.viewmodel.HistoryViewModel

/**
 * History Screen
 */
@Composable
fun HistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier,
    ) {
        Text("History Screen")
    }
}
