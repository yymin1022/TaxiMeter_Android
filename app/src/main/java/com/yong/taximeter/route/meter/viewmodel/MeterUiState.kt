package com.yong.taximeter.route.meter.viewmodel

import com.yong.taximeter.domain.model.MeterStatus

/**
 * UI State for [MeterViewModel]
 */
data class MeterUiState(
    // Current cost
    val currentCost: Int = 0,
    // Cost counter remaining (m)
    val costCounter: Int = 0,
    // Current speed (km/h)
    val currentSpeedKph: Double = 0.0,
    // Total drove distance (m)
    val totalDistanceMeters: Double = 0.0,
    // Meter status
    val meterStatus: MeterStatus = MeterStatus.NOT_RUNNING,
    // Whether city rate is applied
    val isCityRate: Boolean = false,
    // Whether night rate is applied
    val isNightRate: Boolean = false,
)