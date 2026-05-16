package com.yong.taximeter.domain.model

/**
 * Location Data Class
 */
data class LocationData(
    // Latitude value
    val latitude: Double = 0.0,
    // Longitude value
    val longitude: Double = 0.0,
    // GPS Accuracy (meter)
    val accuracyMeters: Float = 0f,
    // GPS Timestamp (ms)
    val timestampMillis: Long = 0L,
)