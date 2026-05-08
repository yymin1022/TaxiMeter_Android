package com.yong.taximeter.domain.model

/**
 * Billing Product Data Class
 */
data class BillingProduct(
    val id: String,
    val name: String,
    val description: String,
    val formattedPrice: String,
    val priceMicros: Long,
)
