package com.yong.taximeter.route.meter.viewmodel

/**
 * UI Effect for Meter
 */
sealed class MeterUiEffect {
    // Show stop confirm dialog
    data class ShowStopDialog(
        val currentCost: Int,
        val totalDistanceMeters: Double,
    ): MeterUiEffect()
}