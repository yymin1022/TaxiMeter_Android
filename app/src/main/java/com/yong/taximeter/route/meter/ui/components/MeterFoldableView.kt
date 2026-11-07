package com.yong.taximeter.route.meter.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.ad.BannerAdView
import com.yong.taximeter.common.window.FoldablePosture
import com.yong.taximeter.route.meter.viewmodel.MeterUiState

/**
 * 폴더블 단말 내부 화면(펼침/반열림)용 미터기 뷰
 */
@Composable
fun MeterFoldableView(
    modifier: Modifier = Modifier,
    posture: FoldablePosture.Available,
    uiState: MeterUiState,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    var containerWindowOffset by remember { mutableStateOf(Offset.Zero) }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                containerWindowOffset = coordinates.positionInWindow()
            },
    ) {
        val density = LocalDensity.current

        if (posture.isVerticalHinge) {
            // 세로 힌지 기준 좌/우 분할 (Book 자세)
            val leftWidth = with(density) {
                maxOf(0.dp, (posture.bounds.left - containerWindowOffset.x).toDp())
            }
            val hingeWidth = with(density) {
                maxOf(0.dp, posture.bounds.width().toDp())
            }
            val rightWidth = with(density) {
                maxOf(0.dp, maxWidth - (posture.bounds.right - containerWindowOffset.x).toDp())
            }

            MeterFoldableVerticalHinge(
                modifier = Modifier.fillMaxSize(),
                leftWidth = leftWidth,
                hingeWidth = hingeWidth,
                rightWidth = rightWidth,
                uiState = uiState,
                onClickStart = onClickStart,
                onClickStop = onClickStop,
                onClickCityRate = onClickCityRate,
                onClickNightRate = onClickNightRate,
            )
        } else {
            // 가로 힌지 기준 상/하 분할 (Tabletop 자세)
            val topHeight = with(density) {
                maxOf(0.dp, (posture.bounds.top - containerWindowOffset.y).toDp())
            }
            val hingeHeight = with(density) {
                maxOf(0.dp, posture.bounds.height().toDp())
            }
            val bottomHeight = with(density) {
                maxOf(0.dp, maxHeight - (posture.bounds.bottom - containerWindowOffset.y).toDp())
            }

            MeterFoldableHorizontalHinge(
                modifier = Modifier.fillMaxSize(),
                topHeight = topHeight,
                hingeHeight = hingeHeight,
                bottomHeight = bottomHeight,
                uiState = uiState,
                onClickStart = onClickStart,
                onClickStop = onClickStop,
                onClickCityRate = onClickCityRate,
                onClickNightRate = onClickNightRate,
            )
        }
    }
}

@Composable
private fun MeterFoldableHorizontalHinge(
    modifier: Modifier = Modifier,
    topHeight: Dp,
    hingeHeight: Dp,
    bottomHeight: Dp,
    uiState: MeterUiState,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        // Upper: Display Section (상단 높이: topHeight, 하단 우측 정렬, 하단 패딩 16dp)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(topHeight),
            contentAlignment = Alignment.BottomEnd,
        ) {
            MeterDisplaySection(
                modifier = Modifier.padding(bottom = 16.dp),
                animationFrames = uiState.animationFrames,
                speedKph = uiState.currentSpeedKph,
                currentCost = uiState.currentCost,
                costCounter = uiState.costCounter,
            )
        }

        // Hinge Spacer
        Spacer(modifier = Modifier.height(hingeHeight))

        // Lower: Info + Control(2x2) + BannerAd (하단 높이: bottomHeight, 하단 정렬)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(bottomHeight),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Info Section (상하 패딩 24dp)
            MeterInfoSection(
                currentSpeedKph = uiState.currentSpeedKph,
                meterStatus = uiState.meterStatus,
                totalDistanceMeters = uiState.totalDistanceMeters,
            )

            // Control Section (GRID_2X2, 하단 패딩 16dp)
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

            // Banner Ad View (하단 패딩 8dp)
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
}

@Composable
private fun MeterFoldableVerticalHinge(
    modifier: Modifier = Modifier,
    leftWidth: Dp,
    hingeWidth: Dp,
    rightWidth: Dp,
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
            // Left: Display Section (폭: leftWidth, 우측 하단 정렬)
            Box(
                modifier = Modifier
                    .width(leftWidth)
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

            // Hinge Spacer (폭: hingeWidth)
            Spacer(modifier = Modifier.width(hingeWidth))

            // Right: Info Section (폭: rightWidth, 중앙 하단 정렬)
            Box(
                modifier = Modifier
                    .width(rightWidth)
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
