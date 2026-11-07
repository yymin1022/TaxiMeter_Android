package com.yong.taximeter.route.meter.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yong.taximeter.R
import com.yong.taximeter.common.ui.theme.MeterTheme

/**
 * 미터기 조작 버튼 섹션
 */
@Composable
fun MeterControlSection(
    modifier: Modifier = Modifier,
    isCityRate: Boolean,
    isNightRate: Boolean,
    onClickStart: () -> Unit,
    onClickStop: () -> Unit,
    onClickCityRate: () -> Unit,
    onClickNightRate: () -> Unit,
) {
    val btnTextCityRate =
        if (isCityRate) stringResource(R.string.meter_btn_percentage_outcity_true)
        else stringResource(R.string.meter_btn_percentage_outcity_false)
    val btnTextNightRate =
        if (isNightRate) stringResource(R.string.meter_btn_percentage_night_true)
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
