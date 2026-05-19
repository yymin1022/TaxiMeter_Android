package com.yong.taximeter.domain.model

/**
 * Meter State Data Class
 */
data class MeterState(
    // Current cost
    val currentCost: Int = 0,
    // Drove distance (m)
    val totalDistanceMeters: Double = 0.0,
    // Drove time (s)
    val totalElapsedSeconds: Double = 0.0,
    // Current speed (km/h)
    val currentSpeedKph: Double = 0.0,
    // Meter status
    val status: MeterStatus = MeterStatus.NOT_RUNNING,
    // Whether night rate is applied
    val isNightRate: Boolean = false,
)