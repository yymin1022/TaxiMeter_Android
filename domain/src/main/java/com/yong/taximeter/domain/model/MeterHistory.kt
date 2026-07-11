package com.yong.taximeter.domain.model

/**
 * Meter History Domain Model
 */
data class MeterHistory(
    val id: Long = 0,
    val timestamp: Long,
    val cost: Int,
    val distanceMeters: Double,
    val elapsedSeconds: Double,
)
