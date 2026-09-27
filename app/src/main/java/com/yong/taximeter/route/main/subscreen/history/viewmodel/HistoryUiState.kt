package com.yong.taximeter.route.main.subscreen.history.viewmodel

import com.yong.taximeter.domain.model.MeterHistory

/**
 * UI State for [HistoryViewModel]
 */
data class HistoryUiState(
    val isLoading: Boolean = true,
    val histories: List<MeterHistory> = emptyList(),
    val totalCount: Int = 0,
    val totalCost: Long = 0L,
    val totalDistanceMeters: Double = 0.0,
    val showClearAllConfirm: Boolean = false,
)
