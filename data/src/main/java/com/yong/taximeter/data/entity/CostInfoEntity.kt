package com.yong.taximeter.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Cost Version Entity
 * - Room DB -> Entity
 */
@Entity(tableName = "cost_info")
data class CostInfoEntity(
    // Region Key
    @PrimaryKey
    val region: String,

    // Base cost info
    val costBase: Int,
    val distBase: Int,

    // Info for cost calculation
    val costRunPer: Int,
    val costTimePer: Int,

    // Extra rate info
    val extraRateCity: Int,
    val extraRateNight1: Int,
    val extraRateNight2: Int,
    val nightStartHour1: Int,
    val nightStartHour2: Int,
    val nightEndHour1: Int,
    val nightEndHour2: Int,

    // Custom flag
    val isCustom: Boolean = false,
)