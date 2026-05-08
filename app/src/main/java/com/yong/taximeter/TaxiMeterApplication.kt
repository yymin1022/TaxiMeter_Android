package com.yong.taximeter

import android.app.Application
import com.yong.taximeter.data.datasource.PreferenceDataSource
import com.yong.taximeter.domain.defs.PreferenceDefs
import com.yong.taximeter.domain.defs.ProductDefs
import com.yong.taximeter.domain.model.PurchaseState
import com.yong.taximeter.domain.repository.BillingRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class TaxiMeterApplication: Application() {
    @Inject lateinit var billingRepository: BillingRepository
    @Inject lateinit var preferenceDataSource: PreferenceDataSource

    private val appScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        appScope.launch {
            billingRepository.connect()
                .onSuccess { syncPurchaseState() }
        }
    }

    /**
     * Get existing purchases, and update ad remove preference
     */
    private suspend fun syncPurchaseState() {
        billingRepository.queryExistingPurchases()
            .onSuccess { purchases ->
                // Get purchased flag
                val purchasedAdRemove = purchases.any { purchase ->
                    purchase.state == PurchaseState.PURCHASED &&
                            purchase.productIDs.contains(ProductDefs.SKU_REMOVE_ADVERTISEMENT)
                }

                // Update Advertisement Removal preference
                preferenceDataSource.setBoolean(PreferenceDefs.PREF_KEY_AD_REMOVE, purchasedAdRemove)
            }
    }
}