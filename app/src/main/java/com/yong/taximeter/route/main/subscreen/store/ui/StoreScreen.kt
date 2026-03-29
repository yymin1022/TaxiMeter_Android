package com.yong.taximeter.route.main.subscreen.store.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.yong.taximeter.route.main.subscreen.store.viewmodel.StoreViewModel

/**
 * Store Screen
 */
@Composable
fun StoreScreen(
    modifier: Modifier = Modifier,
    viewModel: StoreViewModel = hiltViewModel(),
) {
    Box(
        modifier = modifier,
    ) {
        Text("Store Screen")
    }
}