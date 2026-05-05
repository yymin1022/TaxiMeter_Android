package com.yong.taximeter.route.main.subscreen.store.viewmodel

/**
 * UI Effect for Store
 */
sealed class StoreUiEffect {
    // Launch billing flow
    data class LaunchPurchase(
        val productID: String,
    ): StoreUiEffect()
}