package com.yong.taximeter.route.main.subscreen.setting.viewmodel

import com.yong.taximeter.route.main.subscreen.setting.model.SettingItemGroup

/**
 * UI State for [SettingViewModel]
 */
data class SettingUiState(
    // Setting item groups
    val settingGroups: List<SettingItemGroup>? = null,

    // Show Region Setting Dialog
    val showRegionSettingDialog: Boolean = false,
    // Show Theme Setting Dialog
    val showThemeSettingDialog: Boolean = false,
)