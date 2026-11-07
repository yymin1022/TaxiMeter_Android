package com.yong.taximeter.route.meter.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    MeterStandardView(
        modifier = modifier,
        uiState = uiState,
        onClickStart = onClickStart,
        onClickStop = onClickStop,
        onClickCityRate = onClickCityRate,
        onClickNightRate = onClickNightRate,
    )
}
