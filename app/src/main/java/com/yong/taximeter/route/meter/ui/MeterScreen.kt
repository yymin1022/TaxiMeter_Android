package com.yong.taximeter.route.meter.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.room.util.TableInfo
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.dialog.SimpleDialog
import com.yong.taximeter.common.ui.theme.MeterTheme
import com.yong.taximeter.domain.model.MeterStatus
import com.yong.taximeter.route.meter.viewmodel.MeterUiState
import com.yong.taximeter.route.meter.viewmodel.MeterViewModel
import kotlinx.coroutines.delay
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

    // Meter UI Content
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
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
                onClick = navigatePop,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MeterTheme.colors.onBackground,
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
        // Meter Animation
        MeterAnimation(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 16.dp),
            animationFrames = uiState.animationFrames,
            speedKph = uiState.currentSpeedKph,
        )

        // Meter Cost View
        MeterCostView(
            modifier = Modifier,
            uiState = uiState,
        )

        // Mete rInfo
        MeterInfo(
            modifier = Modifier,
            uiState = uiState,
        )

        // Meter Control
        MeterControl(
            modifier = Modifier,
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
            // Meter Animation
            MeterAnimation(
                modifier = Modifier,
                animationFrames = uiState.animationFrames,
                speedKph = uiState.currentSpeedKph,
            )

            // Meter Cost View
            MeterCostView(
                modifier = Modifier,
                uiState = uiState,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            // Meter Info
            MeterInfo(
                modifier = Modifier,
                uiState = uiState,
            )

            // Meter Control
            MeterControl(
                modifier = Modifier,
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

/**
 * Meter Cost View
 */
@Composable
private fun MeterCostView(
    modifier: Modifier = Modifier,
    uiState: MeterUiState,
) {
    val costText = stringResource(
        R.string.meter_cost,
        NumberFormat.getNumberInstance().format(uiState.currentCost),
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.End,
    ) {
        Text(
            text = costText,
            color = MeterTheme.colors.onBackground,
            fontSize = 75.sp,
            fontWeight = FontWeight.Normal,
        )
        Text(
            text = uiState.costCounter.toString(),
            color = MeterTheme.colors.blue,
            fontSize = 35.sp,
        )
    }
}

/**
 * Meter Info
 */
@Composable
private fun MeterInfo(
    modifier: Modifier = Modifier,
    uiState: MeterUiState,
) {
    val distanceText = stringResource(
        R.string.meter_info_distance_data,
        uiState.totalDistanceMeters / 1000,
    )
    val speedText = stringResource(
        R.string.meter_info_speed_data,
        uiState.currentSpeedKph,
    )
    val statusText = when(uiState.meterStatus) {
        MeterStatus.NOT_RUNNING -> stringResource(R.string.meter_info_status_not_running)
        MeterStatus.RUNNING -> stringResource(R.string.meter_info_status_running)
        MeterStatus.GPS_ERROR -> stringResource(R.string.meter_info_status_gps_error)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.End,
        ) {
            // Speed text
            MeterInfoText(
                modifier = Modifier,
                text = stringResource(R.string.meter_info_speed_title),
            )
            MeterInfoText(
                modifier = Modifier,
                text = speedText,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            // Status text
            MeterInfoText(
                modifier = Modifier,
                text = stringResource(R.string.meter_info_status_title),
            )
            MeterInfoText(
                modifier = Modifier,
                text = statusText,
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Distance text
            MeterInfoText(
                modifier = Modifier,
                text = stringResource(R.string.meter_info_distance_title),
            )
            MeterInfoText(
                modifier = Modifier,
                text = distanceText,
            )
        }
    }
}

/**
 * Meter Info Text
 */
@Composable
private fun MeterInfoText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = text,
        color = MeterTheme.colors.onBackground,
        fontSize = 20.sp,
    )
}

/**
 * Meter Control
 */
@Composable
private fun MeterControl(
    modifier: Modifier = Modifier,
    isCityRate: Boolean,
    isNightRate: Boolean,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    val btnTextCityRate =
        if(isCityRate) stringResource(R.string.meter_btn_percentage_outcity_true)
        else stringResource(R.string.meter_btn_percentage_outcity_false)
    val btnTextNightRate =
        if(isNightRate) stringResource(R.string.meter_btn_percentage_night_true)
        else stringResource(R.string.meter_btn_percentage_night_false)

    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            // Start button
            MeterControlButton(
                modifier = Modifier
                    .weight(1f),
                color = MeterTheme.colors.blue,
                text = stringResource(R.string.meter_btn_start),
                onClick = onClickStart,
            )
            // Stop button
            MeterControlButton(
                modifier = Modifier
                    .weight(1f),
                color = MeterTheme.colors.yellow,
                text = stringResource(R.string.meter_btn_stop),
                onClick = onClickStop,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            // Night rate button
            MeterControlButton(
                modifier = Modifier
                    .weight(1f),
                text = btnTextNightRate,
                color = MeterTheme.colors.green,
                onClick = onClickNightRate,
            )

            // City rate button
            MeterControlButton(
                modifier = Modifier
                    .weight(1f),
                text = btnTextCityRate,
                color = MeterTheme.colors.red,
                onClick = onClickCityRate,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

/**
 * Meter Control Button
 */
@Composable
private fun MeterControlButton(
    modifier: Modifier = Modifier,
    color: Color,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 5.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = CircleShape,
        onClick = onClick,
    ) {
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp),
            text = text,
            color = MeterTheme.colors.buttonText,
            fontSize = 17.5.sp,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Meter Animation
 */
@Composable
private fun MeterAnimation(
    modifier: Modifier = Modifier,
    animationFrames: List<Int>,
    speedKph: Double,
) {
    // Exception when frames are empty
    if(animationFrames.isEmpty()) return

    // Animation frame index state
    var frameIndex by remember { mutableIntStateOf(0) }

    // Frame interval based on speed (ms)
    val frameIntervalMs = when {
        speedKph > 50 -> 142L
        speedKph > 30 -> 200L
        speedKph > 20 -> 250L
        speedKph > 10 -> 333L
        speedKph > 0  -> 500L
        else -> 0L  // Stop animation
    }

    // Advance frame index based on interval
    LaunchedEffect(frameIntervalMs) {
        // If 0, stop animation
        if(frameIntervalMs == 0L) return@LaunchedEffect

        while(true) {
            delay(frameIntervalMs)
            frameIndex = (frameIndex + 1) % animationFrames.size
        }
    }

    Box(
        modifier = modifier
            .size(90.dp),
        contentAlignment = Alignment.Center,
    ) {
        val targetFrame = animationFrames[frameIndex]
        Image(
            modifier = Modifier
                .fillMaxSize(),
            painter = painterResource(targetFrame),
            contentDescription = null,
        )
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