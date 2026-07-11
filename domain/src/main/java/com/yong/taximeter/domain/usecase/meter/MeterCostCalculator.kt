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
    // Accumulated extra cost (base cost excluded)
    val accumulatedExtraCost: Int,
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
    // Whether night rate is applied
    val isNightRate: Boolean,
    // Whether city rate is applied
    val isCityRate: Boolean,
    // Current surcharge rate (%)
    val surchargeRate: Int,
) {
    // Dynamic calculation of total cost based only on internal state
    val cost: Int
        get() {
            val baseSurcharge = (costInfo.costBase.toLong() * surchargeRate / 100).toInt()
            return costInfo.costBase + baseSurcharge + accumulatedExtraCost
        }

    companion object {
        /**
         * Init [MeterCostCalculator] instance with [CostInfo]
         */
        fun newWithCostInfo(costInfo: CostInfo) = MeterCostCalculator(
            costInfo = costInfo,
            accumulatedExtraCost = 0,
            costCounter = costInfo.distBase,
            totalDistanceMeters = 0.0,
            totalElapsedSeconds = 0.0,
            currentSpeedKph = 0.0,
            status = MeterStatus.NOT_RUNNING,
            isNightRate = false,
            isCityRate = false,
            surchargeRate = 0,
        )
    }

    /**
     * Convert to [MeterState]
     */
    fun toMeterState() = MeterState(
        currentCost = cost,
        costCounter = costCounter,
        totalDistanceMeters = totalDistanceMeters,
        totalElapsedSeconds = totalElapsedSeconds,
        currentSpeedKph = currentSpeedKph,
        status = status,
        isNightRate = isNightRate,
        isCityRate = isCityRate,
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

        // Get current hour once to maintain state consistency
        val hour = LocalTime.now().hour

        // Check if night rate is enabled at current time
        val isNightRate = checkIsNightRate(hour)
        val newSurchargeRate = calculateSurchargeRate(isCityRate, isNightRate, hour)

        var newAccumulatedExtra = accumulatedExtraCost
        var newCounter = costCounter - distanceDrain - timeDrain

        // Increase cost by unit when counter reaches 0
        while(newCounter <= 0) {
            val unitSurcharge = (MeterDefs.COST_UNIT.toLong() * newSurchargeRate / 100).toInt()
            newAccumulatedExtra += MeterDefs.COST_UNIT + unitSurcharge
            
            newCounter += costInfo.costRunPer
            if(newCounter < 0) newCounter = 0
        }

        return copy(
            accumulatedExtraCost = newAccumulatedExtra,
            costCounter = newCounter,
            totalDistanceMeters = newDistance,
            totalElapsedSeconds = newElapsed,
            currentSpeedKph = speedData.speedKph,
            status = speedData.status,
            isNightRate = isNightRate,
            isCityRate = isCityRate,
            surchargeRate = newSurchargeRate,
        )
    }

    /**
     * Calculate total surcharge rate based on active surcharges
     */
    private fun calculateSurchargeRate(isCityRate: Boolean, isNightRate: Boolean, hour: Int): Int {
        var surchargeRate = 0
        if(isNightRate) {
            surchargeRate += getNightSurchargeRate(hour)
        }
        if(isCityRate) {
            surchargeRate += costInfo.extraRateCity
        }
        return surchargeRate
    }

    /**
     * Get active night surcharge rate based on current hour
     */
    private fun getNightSurchargeRate(hour: Int): Int {
        return if(costInfo.isNightExtra2step) {
            when {
                isInNightRange(hour, costInfo.nightStartHour2, costInfo.nightEndHour2) ->
                    costInfo.extraRateNight2
                isInNightRange(hour, costInfo.nightStartHour1, costInfo.nightEndHour1) ->
                    costInfo.extraRateNight1
                else -> costInfo.extraRateNight1
            }
        } else {
            costInfo.extraRateNight1
        }
    }

    /**
     * Check if night rate is applicable based on current hour
     */
    private fun checkIsNightRate(hour: Int): Boolean {
        return if(costInfo.isNightExtra2step) {
            isInNightRange(hour, costInfo.nightStartHour1, costInfo.nightEndHour1)
                || isInNightRange(hour, costInfo.nightStartHour2, costInfo.nightEndHour2)
        } else {
            isInNightRange(hour, costInfo.nightStartHour1, costInfo.nightEndHour1)
        }
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