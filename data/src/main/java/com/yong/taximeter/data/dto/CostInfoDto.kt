package com.yong.taximeter.data.dto

import com.google.firebase.firestore.PropertyName

/**
 * Cost Info DTO
 * - Firebase Firestore -> DTO
 */
data class CostInfoDto(
    @get:PropertyName("data")
    @PropertyName("data")
    val data: List<CostInfoItem> = emptyList(),
)

data class CostInfoItem(
    // Region Key
    @get:PropertyName("city")
    @PropertyName("city")
    val region: String = "",

    // Cost Info Data
    @get:PropertyName("data")
    @PropertyName("data")
    val data: CostInfoData = CostInfoData(),
)

data class CostInfoData(
    // Base cost info
    @get:PropertyName("cost_base")
    @PropertyName("cost_base")
    val costBase: Int = 0,
    @get:PropertyName("dist_base")
    @PropertyName("dist_base")
    val distBase: Int = 0,

    // Info for cost calculation
    @get:PropertyName("cost_run_per")
    @PropertyName("cost_run_per")
    val costRunPer: Int = 0,
    @get:PropertyName("cost_time_per")
    @PropertyName("cost_time_per")
    val costTimePer: Int = 0,

    // Extra rate info
    @get:PropertyName("perc_city")
    @PropertyName("perc_city")
    val extraRateCity: Int = 0,
    @get:PropertyName("perc_night_1")
    @PropertyName("perc_night_1")
    val extraRateNight1: Int = 0,
    @get:PropertyName("perc_night_2")
    @PropertyName("perc_night_2")
    val extraRateNight2: Int = 0,
    @get:PropertyName("perc_night_start_1")
    @PropertyName("perc_night_start_1")
    val nightStartHour1: Int = 0,
    @get:PropertyName("perc_night_start_2")
    @PropertyName("perc_night_start_2")
    val nightStartHour2: Int = 0,
    @get:PropertyName("perc_night_end_1")
    @PropertyName("perc_night_end_1")
    val nightEndHour1: Int = 0,
    @get:PropertyName("perc_night_end_2")
    @PropertyName("perc_night_end_2")
    val nightEndHour2: Int = 0,
)