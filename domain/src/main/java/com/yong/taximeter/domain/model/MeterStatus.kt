package com.yong.taximeter.domain.model

/**
 * Meter Status Enum
 */
enum class MeterStatus {
    // Meter is not running
    NOT_RUNNING,
    // Meter is running
    RUNNING,
    // Meter is running, but GPS signal is unavailable
    GPS_ERROR,
}