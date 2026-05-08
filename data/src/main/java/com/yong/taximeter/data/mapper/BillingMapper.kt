package com.yong.taximeter.data.mapper

import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.yong.taximeter.domain.model.BillingProduct
import com.yong.taximeter.domain.model.BillingPurchase
import com.yong.taximeter.domain.model.PurchaseState

/**
 * Billing Mapper
 * - [ProductDetails] -> [BillingProduct]
 * - [Purchase] -> [BillingPurchase]
 */
object BillingMapper {
    /**
     * Map IAB [ProductDetails] -> Domain Model
     */
    fun ProductDetails.toBillingProduct() = BillingProduct(
        id = productId,
        name = name,
        description = description,
        formattedPrice = oneTimePurchaseOfferDetails?.formattedPrice ?: "",
        priceMicros = oneTimePurchaseOfferDetails?.priceAmountMicros ?: 0L
    )

    /**
     * Map IAB [Purchase] -> Domain Model
     */
    fun Purchase.toBillingPurchase() = BillingPurchase(
        orderID = orderId,
        productIDs = products,
        purchaseToken = purchaseToken,
        state = getPurchaseStateModel(purchaseState),
        isAcknowledged = isAcknowledged
    )

    /**
     * Map IAB [Purchase.PurchaseState] to Domain Model
     */
    private fun getPurchaseStateModel(state: Int): PurchaseState {
        return when(state) {
            Purchase.PurchaseState.PURCHASED -> PurchaseState.PURCHASED
            Purchase.PurchaseState.PENDING -> PurchaseState.PENDING
            else -> PurchaseState.UNSPECIFIED
        }
    }
}