package com.yong.taximeter.domain.usecase.meter

import com.yong.taximeter.domain.model.MeterStatus

/**
 * Speed Data Class
 * - Calculated from two consecutive LocationData
 */
internal data class SpeedData(
    // Delta distance (m)
    val distanceDeltaMeters: Double = 0.0,
    // Delta elapsed time (s)
    val elapsedDeltaSeconds: Double = 0.0,
    // Current speed (km/h)
    val speedKph: Double = 0.0,
    // GPS Status
    val status: MeterStatus = MeterStatus.RUNNING,
) {
    companion object {
        val ZERO = SpeedData()
    }
}