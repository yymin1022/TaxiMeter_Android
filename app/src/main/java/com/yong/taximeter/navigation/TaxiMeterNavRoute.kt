package com.yong.taximeter.navigation

import kotlinx.serialization.Serializable

/**
 * NavRoute Definitions for Taxi Meter Application
 */
@Serializable
sealed interface TaxiMeterNavRoute {
    // Main
    @Serializable
    data object Main: TaxiMeterNavRoute

    // Meter
    @Serializable
    data object Meter: TaxiMeterNavRoute
}