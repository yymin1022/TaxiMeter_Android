package com.yong.taximeter.route.meter.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.theme.MeterTheme
import kotlinx.coroutines.delay
import java.text.NumberFormat

/**
 * 미터기 말 애니메이션 및 요금/카운터 표시 섹션
 */
@Composable
fun MeterDisplaySection(
    modifier: Modifier = Modifier,
    animationFrames: List<Int>,
    speedKph: Double,
    currentCost: Int,
    costCounter: Int,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
    ) {
        // Meter Animation
        MeterAnimation(
            modifier = Modifier
                .padding(end = 16.dp),
            animationFrames = animationFrames,
            speedKph = speedKph,
        )

        // Meter Cost View
        MeterCostView(
            modifier = Modifier,
            currentCost = currentCost,
            costCounter = costCounter,
        )
    }
}

@Composable
private fun MeterCostView(
    modifier: Modifier = Modifier,
    currentCost: Int,
    costCounter: Int,
) {
    val costText = stringResource(
        R.string.meter_cost,
        NumberFormat.getNumberInstance().format(currentCost),
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
            text = costCounter.toString(),
            color = MeterTheme.colors.blue,
            fontSize = 35.sp,
        )
    }
}

@Composable
private fun MeterAnimation(
    modifier: Modifier = Modifier,
    animationFrames: List<Int>,
    speedKph: Double,
) {
    if (animationFrames.isEmpty()) return

    var frameIndex by remember { mutableIntStateOf(0) }

    val frameIntervalMs = when {
        speedKph > 50 -> 142L
        speedKph > 30 -> 200L
        speedKph > 20 -> 250L
        speedKph > 10 -> 333L
        speedKph > 0 -> 500L
        else -> 0L
    }

    LaunchedEffect(frameIntervalMs) {
        if (frameIntervalMs == 0L) return@LaunchedEffect

        while (true) {
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
