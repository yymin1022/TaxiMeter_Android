package com.yong.taximeter.route.main.viewmodel

import com.yong.taximeter.route.main.model.TabInfo

/**
 * UI State for [MainViewModel]
 */
data class MainUiState(
    val selectedTabIdx: Int? = null,
    val tabList: List<TabInfo>? = null,
)