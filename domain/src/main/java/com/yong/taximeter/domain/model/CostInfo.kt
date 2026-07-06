package com.yong.taximeter.domain.model

/**
 * Cost Info Data Class
 */
data class CostInfo(
    // Region Key
    val region: String = "",

    // Base cost info
    val costBase: Int = 4800,
    val distBase: Int = 1600,

    // Info for cost calculation
    val costRunPer: Int = 131,
    val costTimePer: Int = 30,

    // Extra rate info
    val extraRateCity: Int = 20,
    val extraRateNight1: Int = 20,
    val extraRateNight2: Int = 40,
    val nightStartHour1: Int = 22,
    val nightStartHour2: Int = 23,
    val nightEndHour1: Int = 4,
    val nightEndHour2: Int = 2,
) {
    // is night extra rate is 2-step
    val isNightExtra2step: Boolean
        get() = (nightStartHour1 != nightStartHour2)
                || (nightEndHour1 != nightEndHour2)
}