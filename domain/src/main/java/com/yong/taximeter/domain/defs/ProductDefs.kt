package com.yong.taximeter.domain.defs

/**
 * Product Definitions
 */
object ProductDefs {
    // Product SKU IDs
    const val SKU_REMOVE_ADVERTISEMENT = "ad_remove"
    const val SKU_DONATE_1000 = "donation_1000"
    const val SKU_DONATE_5000 = "donation_5000"
    const val SKU_DONATE_10000 = "donation_10000"
    const val SKU_DONATE_50000 = "donation_50000"

    // Acknowledgeable Products (One-time purchasable)
    val PRODUCTS_ACKNOWLEDGEABLE = listOf(
        SKU_REMOVE_ADVERTISEMENT
    )

    // Consumable Products (Re-purchasable)
    val PRODUCTS_CONSUMABLE = listOf(
        SKU_DONATE_1000,
        SKU_DONATE_5000,
        SKU_DONATE_10000,
        SKU_DONATE_50000,
    )

    // All Products
    val PRODUCTS_ALL = PRODUCTS_ACKNOWLEDGEABLE + PRODUCTS_CONSUMABLE
}