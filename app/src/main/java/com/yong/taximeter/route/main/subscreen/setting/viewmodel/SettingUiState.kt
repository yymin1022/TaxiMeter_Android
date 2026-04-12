package com.yong.taximeter.route.main.subscreen.setting.viewmodel

import androidx.annotation.StringRes
import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.route.main.subscreen.setting.model.SettingItemGroup

/**
 * UI State for [SettingViewModel]
 */
data class SettingUiState(
    // Setting item groups
    val settingGroups: List<SettingItemGroup>? = null,

    // Show Dialog
    val showDialog: ShowDialog = ShowDialog.Nothing,
)

sealed class ShowDialog {
    // Not show any dialog
    data object Nothing: ShowDialog()

    // Show Custom Cost Dialog
    data class CustomCostDialog(
        @StringRes
        val titleRes: Int,
        val onComplete: (costInfo: CostInfo) -> Unit,
    ): ShowDialog()

    // Show Radio Select Dialog (Region / Theme)
    data class RadioSelectDialog(
        @StringRes
        val titleRes: Int,
        val itemTextResources: List<Int>,
        val onComplete: ((selectedIdx: Int) -> Unit),
    ): ShowDialog()
}