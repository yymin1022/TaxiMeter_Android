package com.yong.taximeter.domain.model

/**
 * Billing Purchase Data Class
 */
data class BillingPurchase(
    val orderID: String?,
    val productIDs: List<String>,
    val purchaseToken: String,
    val state: PurchaseState,
    val isAcknowledged: Boolean,
)

/**
 * Purchase State Enum
 */
enum class PurchaseState {
    PURCHASED,
    PENDING,
    UNSPECIFIED,
}