package com.yong.taximeter.data.repository

import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.yong.taximeter.core.common.AppLogger
import com.yong.taximeter.data.mapper.BillingMapper.toBillingProduct
import com.yong.taximeter.data.mapper.BillingMapper.toBillingPurchase
import com.yong.taximeter.domain.model.BillingProduct
import com.yong.taximeter.domain.model.BillingPurchase
import com.yong.taximeter.domain.repository.BillingRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class BillingRepositoryImpl @Inject constructor(
    // Inject Android Context
    context: Context,
    // Inject App Logger
    private val logger: AppLogger,
): BillingRepository {
    companion object {
        private const val LOG_TAG = "BillingRepository"
    }

    // Purchase event channel
    // - All successful purchased event is buffered
    private val _purchaseChannel = Channel<Result<List<BillingPurchase>>>(Channel.BUFFERED)

    // Product detail information cache
    // - Key: Product ID
    // - Value: ProductDetails from Billing Client
    private val productDetailsCache = mutableMapOf<String, ProductDetails>()

    // Google Play Billing Client
    // - Process result with handlePurchasesUpdated method
    private val billingClient: BillingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener { billingResult, purchases ->
            handlePurchasesUpdated(billingResult, purchases)
        }
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    /**
     * Connect to billing system of Google Play
     */
    override suspend fun connect(): Result<Unit> {
        // Check if already connected
        if(billingClient.isReady) {
            return Result.success(Unit)
        }

        // Try to connect Billing Client
        var connectResult: Result<Unit> = Result.failure(Exception("Billing loading"))
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                connectResult =
                    if(result.responseCode == BillingClient.BillingResponseCode.OK) {
                        logger.log("Billing connected")
                        Result.success(Unit)
                    } else {
                        // Log exception
                        val exception = Exception("Failed to connect(${result.responseCode}): ${result.debugMessage}")
                        logger.recordException(exception)
                        Result.failure(exception)
                    }
            }

            override fun onBillingServiceDisconnected() {
                logger.log("Billing disconnected")
            }
        })

        return connectResult
    }

    /**
     * Disconnect with billing system
     */
    override fun disconnect() {
        if(billingClient.isReady) {
            billingClient.endConnection()
            logger.log("Billing disconnected")
        }
    }

    /**
     * Observe to purchase event channel
     */
    override fun observePurchases(): Flow<Result<List<BillingPurchase>>> {
        return _purchaseChannel.receiveAsFlow()
    }

    /**
     * Handle purchased information
     * - If purchase response is OK, process as Success
     * - If not OK, process as Failure
     */
    private fun handlePurchasesUpdated(
        billingResult: BillingResult,
        purchases: List<Purchase>?
    ) {
        // Process with response
        val result = if(billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            Result.success(purchases?.map { it.toBillingPurchase() } ?: emptyList())
        } else {
            // Log exception
            val exception = Exception("Failed to purchase(${billingResult.responseCode}): ${billingResult.debugMessage}")
            logger.recordException(exception)
            Result.failure(exception)
        }

        // Send to Channel
        _purchaseChannel.trySend(result)
    }

    /**
     * Acknowledge purchase item
     * - ex) Remove Advertisement
     */
    override suspend fun acknowledgePurchase(purchaseToken: String): Result<Unit> {
        // Check if client is ready
        if(billingClient.isReady.not()) {
            connect()
        }

        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()

        var ackResult: Result<Unit> = Result.failure(Exception("Loading"))
        billingClient.acknowledgePurchase(params) { billingResult ->
            ackResult = if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                logger.logEvent(LOG_TAG, mapOf(Pair("Acknowledged product", billingResult.debugMessage)))
                Result.success(Unit)
            } else {
                // Log exception
                val exception = Exception("Failed to acknowledge purchases(${billingResult.responseCode}): ${billingResult.debugMessage}")
                logger.recordException(exception)
                Result.failure(exception)
            }
        }

        return ackResult
    }

    /**
     * Consume purchase item
     * - ex) Donation
     */
    override suspend fun consumePurchase(purchaseToken: String): Result<Unit> {
        // Check if client is ready
        if(billingClient.isReady.not()) {
            connect()
        }

        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(purchaseToken)
            .build()

        var consumeResult: Result<Unit> = Result.failure(Exception("Loading purchase"))
        billingClient.consumeAsync(params) { billingResult, _ ->
            consumeResult = if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                logger.logEvent(LOG_TAG, mapOf(Pair("Consumed product", billingResult.debugMessage)))
                Result.success(Unit)
            } else {
                // Log exception
                val exception = Exception("Failed to consume purchases(${billingResult.responseCode}): ${billingResult.debugMessage}")
                logger.recordException(exception)
                Result.failure(exception)
            }
        }

        return consumeResult
    }

    /**
     * Get currently purchased items
     */
    override suspend fun queryExistingPurchases(): Result<List<BillingPurchase>> {
        // Check if client is ready
        if(billingClient.isReady.not()) {
            connect()
        }

        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.INAPP)
            .build()

        var queryResult: Result<List<BillingPurchase>> = Result.failure(Exception("Loading purchases"))
        billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
            queryResult =
                if(billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    Result.success(purchasesList.map { it.toBillingPurchase() })
                } else {
                    // Log exception
                    val exception = Exception("Failed to load purchases(${billingResult.responseCode}): ${billingResult.debugMessage}")
                    logger.recordException(exception)
                    Result.failure(exception)
                }
        }

        return queryResult
    }

    /**
     * Get currently available products
     */
    override suspend fun queryProducts(productIds: List<String>): Result<List<BillingProduct>> {
        // Check if client is ready
        if(billingClient.isReady.not()) {
            connect()
        }

        // Get product list from Billing Client
        val productList = productIds.map { id ->
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(id)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()

        // Query Product information from Billing Client
        var queryResult: Result<List<BillingProduct>> = Result.failure(Exception("Loading products"))
        billingClient.queryProductDetailsAsync(params) { billingResult, detailsList ->
            queryResult =
                if(billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    detailsList.productDetailsList.forEach { productDetailsCache[it.productId] = it }
                    Result.success(detailsList.productDetailsList.map { it.toBillingProduct() })
                } else {
                    // Log exception
                    val exception = Exception("Failed to load products(${billingResult.responseCode}): ${billingResult.debugMessage}")
                    logger.recordException(exception)
                    Result.failure(exception)
                }
        }

        return queryResult
    }
}