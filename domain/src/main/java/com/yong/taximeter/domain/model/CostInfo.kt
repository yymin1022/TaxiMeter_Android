package com.yong.taximeter.domain.model

/**
 * Cost Info Data Class
 */
data class CostInfo(
    // Region Key
    val region: String = "",

    // Base cost info
    val costBase: Int = 0,
    val distBase: Int = 0,

    // Info for cost calculation
    val costRunPer: Int = 0,
    val costTimePer: Int = 0,

    // Extra rate info
    val extraRateCity: Int = 0,
    val extraRateNight1: Int = 0,
    val extraRateNight2: Int = 0,
    val nightStartHour1: Int = 0,
    val nightStartHour2: Int = 0,
    val nightEndHour1: Int = 0,
    val nightEndHour2: Int = 0,
)