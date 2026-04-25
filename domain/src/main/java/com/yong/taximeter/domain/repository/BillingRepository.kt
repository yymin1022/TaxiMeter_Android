package com.yong.taximeter.domain.repository

import kotlinx.coroutines.flow.Flow
import com.yong.taximeter.domain.model.BillingProduct
import com.yong.taximeter.domain.model.BillingPurchase

/**
 * Billing Repository Interface
 * - Get product information
 * - Get purchased information
 * - Launch purchase processing
 */
interface BillingRepository {
    // Connect / Disconnect Billing Service
    suspend fun connect(): Result<Unit>
    fun disconnect()

    // Observe for Billing
    fun observePurchases(): Flow<Result<List<BillingPurchase>>>

    // Manage Purchase
    fun acknowledgePurchase(purchaseToken: String): Result<Unit>
    fun consumePurchase(purchaseToken: String): Result<Unit>
    fun queryExistingPurchases(): Result<List<BillingPurchase>>
    fun queryProducts(productIds: List<String>): Result<List<BillingProduct>>
}