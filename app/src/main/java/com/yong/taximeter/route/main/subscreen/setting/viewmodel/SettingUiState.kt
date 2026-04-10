package com.yong.taximeter.route.main.subscreen.setting.viewmodel

import com.yong.taximeter.route.main.subscreen.store.model.SettingItemGroup

/**
 * UI State for [SettingViewModel]
 */
data class SettingUiState(
    // Setting item groups
    val settingGroups: List<SettingItemGroup>? = null,
)