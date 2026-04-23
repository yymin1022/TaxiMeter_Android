package com.yong.taximeter.data.repository

import com.yong.taximeter.domain.model.BillingProduct
import com.yong.taximeter.domain.model.BillingPurchase
import com.yong.taximeter.domain.repository.BillingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BillingRepositoryImpl @Inject constructor(
    // Inject dependencies
): BillingRepository {
    /**
     * Connect to billing system of Google Play
     */
    override fun connect(): Result<Unit> {
        TODO("Not yet implemented")
    }

    /**
     * Disconnect with billing system
     */
    override fun disconnect() {
        TODO("Not yet implemented")
    }

    /**
     * Observe to purchase event,
     */
    override fun observePurchases(): Flow<Result<List<BillingPurchase>>> {
        TODO("Not yet implemented")
    }

    /**
     * Acknowledge purchase item
     * - ex) Remove Advertisement
     */
    override fun acknowledgePurchase(purchaseToken: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    /**
     * Consume purchase item
     * - ex) Donation
     */
    override fun consumePurchase(purchaseToken: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    /**
     * Get currently purchased items
     */
    override fun queryExistingPurchases(): Result<List<BillingPurchase>> {
        TODO("Not yet implemented")
    }

    /**
     * Get currently available products
     */
    override fun queryProducts(productIds: List<String>): Result<List<BillingProduct>> {
        TODO("Not yet implemented")
    }
}