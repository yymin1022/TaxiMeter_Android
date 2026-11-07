package com.yong.taximeter.route.meter.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.ad.BannerAdView
import com.yong.taximeter.common.ui.dialog.SimpleDialog
import com.yong.taximeter.common.ui.theme.MeterTheme
import com.yong.taximeter.domain.model.MeterStatus
import com.yong.taximeter.route.meter.ui.components.MeterControlSection
import com.yong.taximeter.route.meter.ui.components.MeterDisplaySection
import com.yong.taximeter.route.meter.ui.components.MeterInfoSection
import com.yong.taximeter.route.meter.viewmodel.MeterUiState
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
    navigatePop: () -> Unit,
) {
    // UI State
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Track whether the exit warning dialog should show (triggered by back request)
    var showExitDialog by remember { mutableStateOf(false) }

    // Handle system back gesture
    BackHandler(enabled = uiState.meterStatus != MeterStatus.NOT_RUNNING) {
        when {
            uiState.showStopDialog -> viewModel.onCancelStop()
            showExitDialog -> showExitDialog = false
            else -> showExitDialog = true
        }
    }

    // Keep screen awake while meter is running
    val isKeepScreenOn = uiState.meterStatus != MeterStatus.NOT_RUNNING
    val currentView = LocalView.current
    DisposableEffect(isKeepScreenOn) {
        currentView.keepScreenOn = isKeepScreenOn
        onDispose {
            currentView.keepScreenOn = false
        }
    }

    // SnackBar State
    val snackBarHostState = remember { SnackbarHostState() }
    // SnackBar Effect
    val snackBarMessageRes = uiState.snackBarMessageRes
    snackBarMessageRes?.let {
        val message = stringResource(it)
        LaunchedEffect(message) {
            // Show Snack Bar
            snackBarHostState.showSnackbar(message)
            // Clear Snack Bar Message
            viewModel.clearSnackBar()
        }
    }

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

    // Exit Warning Dialog
    if (showExitDialog) {
        SimpleDialog(
            titleRes = R.string.meter_dialog_stop_title,
            descRes = R.string.meter_dialog_stop_exit_content,
            onConfirm = {
                showExitDialog = false
                viewModel.onConfirmStop()
                navigatePop()
            },
            onDismiss = {
                showExitDialog = false
            }
        )
    }

    // Legal Warning Dialog
    val showLegalWarningDialog = uiState.showLegalWarningDialog
    if (showLegalWarningDialog) {
        SimpleDialog(
            titleRes = R.string.meter_dialog_legal_warning_title,
            descRes = R.string.meter_dialog_legal_warning_content,
            onConfirm = viewModel::onConfirmLegalWarning,
            onDismiss = {
                viewModel.onDismissLegalWarning()
                navigatePop()
            },
        )
    }

    // Permission Warning Dialog
    val showPermissionWarningDialog = uiState.showPermissionWarningDialog
    val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    if (showPermissionWarningDialog) {
        SimpleDialog(
            titleRes = R.string.meter_dialog_permission_warning_title,
            descRes = R.string.meter_dialog_permission_warning_content,
            onConfirm = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (notificationPermission?.status?.isGranted == false) {
                        notificationPermission.launchPermissionRequest()
                    }
                }
                viewModel.onConfirmPermissionWarning()
            },
            onDismiss = {
                viewModel.onDismissPermissionWarning()
            }
        )
    }

    // Meter UI Content
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                // Location permission state
                val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

                val onClickStart =  {
                    // Start meter only location permission is granted
                    if(locationPermission.status.isGranted) {
                        // Checks permission with AccompanistPermission API, so suppress warning
                        @SuppressLint("MissingPermission")
                        viewModel.onClickStart()
                    } else {
                        locationPermission.launchPermissionRequest()
                    }
                }

                // Meter Content
                MeterContent(
                    uiState = uiState,
                    onClickStart = onClickStart,
                    onClickStop = viewModel::onClickStop,
                    onClickCityRate = viewModel::onClickCityRate,
                    onClickNightRate = viewModel::onClickNightRate,
                )

                // Back button
                IconButton(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(4.dp),
                    onClick = {
                        if (uiState.meterStatus != MeterStatus.NOT_RUNNING) {
                            showExitDialog = true
                        } else {
                            navigatePop()
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = MeterTheme.colors.onBackground,
                    )
                }
            }

            if (!uiState.isAdRemoved) {
                BannerAdView(
                    modifier = Modifier
                        .fillMaxWidth(),
                    adUnitId = stringResource(id = R.string.admob_banner_ad_unit_id),
                )
            }
        }
    }
}

/**
 * Meter Content UI
 */
@Composable
private fun MeterContent(
    uiState: MeterUiState,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    // Screen orientation state
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    if(isLandscape) {
        MeterContentLandscape(
            uiState = uiState,
            onClickStart = onClickStart,
            onClickStop = onClickStop,
            onClickCityRate = onClickCityRate,
            onClickNightRate = onClickNightRate,
        )
    } else {
        MeterContentPortrait(
            uiState = uiState,
            onClickStart = onClickStart,
            onClickStop = onClickStop,
            onClickCityRate = onClickCityRate,
            onClickNightRate = onClickNightRate,
        )
    }
}

/**
 * Portrait Meter Content UI
 */
@Composable
private fun MeterContentPortrait(
    uiState: MeterUiState,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom,
    ) {
        // Meter Display Section
        MeterDisplaySection(
            animationFrames = uiState.animationFrames,
            speedKph = uiState.currentSpeedKph,
            currentCost = uiState.currentCost,
            costCounter = uiState.costCounter,
        )

        // Meter Info Section
        MeterInfoSection(
            currentSpeedKph = uiState.currentSpeedKph,
            meterStatus = uiState.meterStatus,
            totalDistanceMeters = uiState.totalDistanceMeters,
        )

        // Meter Control Section
        MeterControlSection(
            isCityRate = uiState.isCityRate,
            isNightRate = uiState.isNightRate,
            onClickStart = onClickStart,
            onClickStop = onClickStop,
            onClickCityRate = onClickCityRate,
            onClickNightRate = onClickNightRate,
        )
    }
}

/**
 * Landscape Meter Content UI
 */
@Composable
private fun MeterContentLandscape(
    uiState: MeterUiState,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom,
        ) {
            // Meter Display Section
            MeterDisplaySection(
                animationFrames = uiState.animationFrames,
                speedKph = uiState.currentSpeedKph,
                currentCost = uiState.currentCost,
                costCounter = uiState.costCounter,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            // Meter Info Section
            MeterInfoSection(
                currentSpeedKph = uiState.currentSpeedKph,
                meterStatus = uiState.meterStatus,
                totalDistanceMeters = uiState.totalDistanceMeters,
            )

            // Meter Control Section
            MeterControlSection(
                isCityRate = uiState.isCityRate,
                isNightRate = uiState.isNightRate,
                onClickStart = onClickStart,
                onClickStop = onClickStop,
                onClickCityRate = onClickCityRate,
                onClickNightRate = onClickNightRate,
            )
        }
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