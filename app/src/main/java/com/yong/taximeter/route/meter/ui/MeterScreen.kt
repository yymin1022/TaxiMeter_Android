package com.yong.taximeter.route.meter.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.dialog.SimpleDialog
import com.yong.taximeter.route.meter.viewmodel.MeterViewModel
import java.text.NumberFormat

/**
 * Meter Screen
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MeterScreen(
    modifier: Modifier = Modifier,
    viewModel: MeterViewModel = hiltViewModel(),
) {
    // UI State
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Stop Dialog
    val showStopDialog = uiState.showStopDialog
    if(showStopDialog) {
        MeterStopDialog(
            currentCost = uiState.currentCost,
            totalDistanceMeters = uiState.totalDistanceMeters,
            onConfirm = viewModel::onConfirmStop,
            onDismiss = viewModel::onCancelStop,
        )
    }

    Box(
        modifier = modifier,
    ) {
        Text("Meter Screen")
    }
}

@Composable
private fun MeterStopDialog(
    currentCost: Int,
    totalDistanceMeters: Double,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val titleText = stringResource(R.string.meter_dialog_stop_title)
    val descText = stringResource(
        R.string.meter_dialog_stop_content,
        NumberFormat.getNumberInstance().format(currentCost),
        NumberFormat.getNumberInstance().format(totalDistanceMeters / 1000),
    )

    SimpleDialog(
        title = titleText,
        desc = descText,
        onConfirm = onConfirm,
        onDismiss = onDismiss,
    )
}