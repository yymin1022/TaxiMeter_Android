package com.yong.taximeter.domain.usecase.meter

import com.yong.taximeter.domain.defs.MeterDefs
import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.model.MeterState
import com.yong.taximeter.domain.model.MeterStatus
import java.time.LocalTime

/**
 * Meter Cost Calculator Data Class
 * - Calculates and accumulates cost from SpeedData
 * - Call [newWithCostInfo] to init with [CostInfo]
 */
data class MeterCostCalculator(
    // Cost info
    val costInfo: CostInfo,
    // Current cost
    val cost: Int,
    // Cost counter remaining (m)
    val costCounter: Int,
    // Total drove distance (m)
    val totalDistanceMeters: Double,
    // Total elapsed time (s)
    val totalElapsedSeconds: Double,
    // Current speed (km/h)
    val currentSpeedKph: Double,
    // Meter status
    val status: MeterStatus,
) {
    companion object {
        /**
         * Init [MeterCostCalculator] instance with [CostInfo]
         */
        fun newWithCostInfo(costInfo: CostInfo) = MeterCostCalculator(
            costInfo = costInfo,
            cost = costInfo.costBase,
            costCounter = costInfo.distBase,
            totalDistanceMeters = 0.0,
            totalElapsedSeconds = 0.0,
            currentSpeedKph = 0.0,
            status = MeterStatus.NOT_RUNNING,
        )
    }

    /**
     * Convert to [MeterState]
     */
    fun toMeterState() = MeterState(
        currentCost = cost,
        totalDistanceMeters = totalDistanceMeters,
        totalElapsedSeconds = totalElapsedSeconds,
        currentSpeedKph = currentSpeedKph,
        status = status,
    )

    /**
     * Update cost and distance with [SpeedData]
     * - Drain cost counter by distance and time
     * - Increase cost by [MeterDefs.COST_UNIT] when counter reaches 0
     *
     * @param speedData Speed data to update with
     * @param isCityRate Whether to apply city surcharge
     */
    fun update(speedData: SpeedData, isCityRate: Boolean): MeterCostCalculator {
        val newDistance = totalDistanceMeters + speedData.distanceDeltaMeters
        val newElapsed = totalElapsedSeconds + speedData.elapsedDeltaSeconds

        // Drain counter by distance
        val distanceDrain = speedData.distanceDeltaMeters.toInt()
        // Drain counter by time when speed is below threshold
        val timeDrain = if(speedData.speedKph < MeterDefs.SPEED_THRESHOLD_KPH) {
            (costInfo.costRunPer.toDouble() / costInfo.costTimePer * speedData.elapsedDeltaSeconds).toInt()
        } else 0

        var newCost = cost
        var newCounter = costCounter - distanceDrain - timeDrain

        // Increase cost by unit when counter reaches 0
        while(newCounter <= 0) {
            newCost += MeterDefs.COST_UNIT
            newCost += calculateSurcharge(isCityRate)
            newCounter += costInfo.costRunPer

            if(newCounter < 0) newCounter = 0
        }

        return copy(
            cost = newCost,
            costCounter = newCounter,
            totalDistanceMeters = newDistance,
            totalElapsedSeconds = newElapsed,
            currentSpeedKph = speedData.speedKph,
            status = speedData.status,
        )
    }

    /**
     * Calculate surcharge per [MeterDefs.COST_UNIT] increase
     * - Apply night surcharge based on current hour
     * - Apply city surcharge if [isCityRate] is true
     *
     * @param isCityRate Whether to apply city extra rate
     */
    private fun calculateSurcharge(isCityRate: Boolean): Int {
        var surcharge = 0

        // Apply night surcharge
        val hour = LocalTime.now().hour
        if(costInfo.isNightExtra2step) {
            surcharge += when {
                isInNightRange(hour, costInfo.nightStartHour2, costInfo.nightEndHour2) ->
                    costInfo.extraRateNight2
                isInNightRange(hour, costInfo.nightStartHour1, costInfo.nightEndHour1) ->
                    costInfo.extraRateNight1
                else -> 0
            }
        } else {
            if (isInNightRange(hour, costInfo.nightStartHour1, costInfo.nightEndHour1)) {
                surcharge += costInfo.extraRateNight1
            }
        }

        // Apply city surcharge
        if(isCityRate) surcharge += costInfo.extraRateCity

        return surcharge
    }

    /**
     * Check if [hour] is in night range
     * - Handle midnight crossing (e.g. 23 ~ 02)
     */
    private fun isInNightRange(hour: Int, start: Int, end: Int): Boolean {
        return if(start > end) hour !in end until start
        else hour in start until end
    }
}