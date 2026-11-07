package com.yong.taximeter.route.meter.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.yong.taximeter.route.meter.viewmodel.MeterUiState

/**
 * 일반 기기 및 커버 화면용 미터기 표준 뷰
 */
@Composable
fun MeterStandardView(
    modifier: Modifier = Modifier,
    uiState: MeterUiState,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
    if (isLandscape) {
        MeterStandardLandscape(
            modifier = modifier,
            uiState = uiState,
            onClickStart = onClickStart,
            onClickStop = onClickStop,
            onClickCityRate = onClickCityRate,
            onClickNightRate = onClickNightRate,
        )
    } else {
        MeterStandardPortrait(
            modifier = modifier,
            uiState = uiState,
            onClickStart = onClickStart,
            onClickStop = onClickStop,
            onClickCityRate = onClickCityRate,
            onClickNightRate = onClickNightRate,
        )
    }
}

@Composable
private fun MeterStandardPortrait(
    modifier: Modifier = Modifier,
    uiState: MeterUiState,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
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

@Composable
private fun MeterStandardLandscape(
    modifier: Modifier = Modifier,
    uiState: MeterUiState,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    Row(
        modifier = modifier
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
