package com.yong.taximeter.route.meter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.theme.MeterTheme
import com.yong.taximeter.domain.model.MeterStatus

/**
 * 미터기 주행 정보(현재 속도, 주행 상태, 주행 거리) 표시 섹션
 */
@Composable
fun MeterInfoSection(
    modifier: Modifier = Modifier,
    currentSpeedKph: Double,
    meterStatus: MeterStatus,
    totalDistanceMeters: Double,
) {
    val distanceText = stringResource(
        R.string.meter_info_distance_data,
        totalDistanceMeters / 1000,
    )
    val speedText = stringResource(
        R.string.meter_info_speed_data,
        currentSpeedKph,
    )
    val statusText = when (meterStatus) {
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

@Composable
private fun MeterInfoText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Text(
        modifier = modifier,
        text = text,
        color = MeterTheme.colors.onBackground,
        fontSize = 20.sp,
    )
}
