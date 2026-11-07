package com.yong.taximeter.route.meter.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.ad.BannerAdView
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
        Spacer(modifier = Modifier.weight(1f))

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
            modifier = Modifier.padding(bottom = 16.dp),
            layout = MeterControlLayout.GRID_2X2,
            isCityRate = uiState.isCityRate,
            isNightRate = uiState.isNightRate,
            onClickStart = onClickStart,
            onClickStop = onClickStop,
            onClickCityRate = onClickCityRate,
            onClickNightRate = onClickNightRate,
        )

        if (!uiState.isAdRemoved) {
            BannerAdView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                adUnitId = stringResource(id = R.string.admob_banner_ad_unit_id),
            )
        }
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
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            // Upper Left: Meter Display Section (우측 하단 정렬, 50% 폭)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomEnd,
            ) {
                MeterDisplaySection(
                    animationFrames = uiState.animationFrames,
                    speedKph = uiState.currentSpeedKph,
                    currentCost = uiState.currentCost,
                    costCounter = uiState.costCounter,
                )
            }

            // Upper Right: Meter Info Section (중앙 하단 정렬, 50% 폭)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                MeterInfoSection(
                    currentSpeedKph = uiState.currentSpeedKph,
                    meterStatus = uiState.meterStatus,
                    totalDistanceMeters = uiState.totalDistanceMeters,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lower: Meter Control Section (1x4 단일 행, 하단 간격 12dp)
        MeterControlSection(
            modifier = Modifier.padding(bottom = 12.dp),
            layout = MeterControlLayout.HORIZONTAL_1X4,
            isCityRate = uiState.isCityRate,
            isNightRate = uiState.isNightRate,
            onClickStart = onClickStart,
            onClickStop = onClickStop,
            onClickCityRate = onClickCityRate,
            onClickNightRate = onClickNightRate,
        )

        // Bottom: Banner Ad View (하단 간격 6dp)
        if (!uiState.isAdRemoved) {
            BannerAdView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                adUnitId = stringResource(id = R.string.admob_banner_ad_unit_id),
            )
        }
    }
}
