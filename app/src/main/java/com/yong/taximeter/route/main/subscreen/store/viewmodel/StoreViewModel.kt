package com.yong.taximeter.route.main.subscreen.store.viewmodel

import android.app.Activity
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.taximeter.R
import com.yong.taximeter.data.repository.BillingRepositoryImpl
import com.yong.taximeter.domain.model.BillingProduct
import com.yong.taximeter.domain.model.PurchaseState
import com.yong.taximeter.domain.repository.BillingRepository
import com.yong.taximeter.route.main.subscreen.store.model.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    // Inject Billing Repository
    private val billingRepository: BillingRepository,
): ViewModel() {
    companion object {
        // Product SKU IDs
        private const val SKU_REMOVE_ADVERTISEMENT = "ad_remove"
        private const val SKU_DONATE_1000 = "donation_1000"
        private const val SKU_DONATE_5000 = "donation_5000"
        private const val SKU_DONATE_10000 = "donation_10000"
        private const val SKU_DONATE_50000 = "donation_50000"

        // Acknowledgeable Products (One-time purchasable)
        private val PRODUCTS_ACKNOWLEDGEABLE = listOf(
            SKU_REMOVE_ADVERTISEMENT
        )

        // Consumable Products (Re-purchasable)
        private val PRODUCTS_CONSUMABLE = listOf(
            SKU_DONATE_1000,
            SKU_DONATE_5000,
            SKU_DONATE_10000,
            SKU_DONATE_50000,
        )

        // All Products
        private val PRODUCTS_ALL = PRODUCTS_ACKNOWLEDGEABLE + PRODUCTS_CONSUMABLE
    }

    // UI State
    private val _uiState: MutableStateFlow<StoreUiState> = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()

    // UI Effect
    private val _uiEffect: Channel<StoreUiEffect> = Channel(Channel.BUFFERED)
    val uiEffect: Flow<StoreUiEffect> = _uiEffect.receiveAsFlow()

    // Product IDs
    private var currentSelectedProductID: String? = null
    private var productIDs: List<String> = emptyList()

    /**
     * Load product items and convert to UI Item
     */
    fun loadProducts() {
        viewModelScope.launch {
            // Set loading true
            setLoading(true)

            // Load products via repository
            billingRepository.queryProducts(PRODUCTS_ALL)
                .onFailure {
                    // Show error message to snack bar
                    showSnackBar(R.string.store_snack_bar_load_product_fail)
                }
                .onSuccess { products ->
                    // Update cached product id info
                    this@StoreViewModel.productIDs = products.map { it.id }

                    // Get purchased products
                    var purchasedProductIDs = emptyList<String>()
                    billingRepository.queryExistingPurchases()
                        .onFailure {
                            // Show error message to snack bar
                            showSnackBar(R.string.store_snack_bar_load_purchase_fail)
                        }
                        .onSuccess { purchases ->
                            // Update purchased products info
                            // - Filter already purchased, and NOT re-purchasable products only
                            purchasedProductIDs = purchases
                                .filter { it.state == PurchaseState.PURCHASED }
                                .flatMap { it.productIDs }
                                .filter { PRODUCTS_ACKNOWLEDGEABLE.contains(it) }
                        }

                    // Generate product items
                    val productItems = products.map { product ->
                        // Purchased flag
                        val isPurchased = purchasedProductIDs.contains(product.id)

                        // Convert to item
                        product.asProductItem(isPurchased)
                    }

                    // Update UI State
                    _uiState.update {
                        it.copy(
                            productItems = productItems,
                        )
                    }
                }

            // Set loading false
            setLoading(false)
        }
    }

    /**
     * On click product item
     */
    fun onClickProduct(idx: Int) {
        // Check index range
        if(productIDs.indices.contains(idx).not()) {
            this.currentSelectedProductID = null
            return
        }

        // Update selected product
        this.currentSelectedProductID = productIDs[idx]
        // Update UI State
        _uiState.update {
            it.copy(
                selectedProductIdx = idx,
            )
        }
    }

    /**
     * On click purchase button
     */
    fun onClickPurchase() {
        viewModelScope.launch {
            // Launch only selected product is effective
            val productID = this@StoreViewModel.currentSelectedProductID ?: return@launch

            // Send UI Effect
            _uiEffect.send(
                StoreUiEffect.LaunchPurchase(
                    productID = productID,
                )
            )
        }
    }

    /**
     * Launch billing flow with [BillingRepositoryImpl]
     */
    fun launchBillingFlow(activity: Activity, productID: String) {
        val repo = billingRepository as BillingRepositoryImpl
        repo.launchPurchase(activity, productID)
            .onSuccess {
                _uiState.update {
                    it.copy(
                        isPurchasing = true,
                    )
                }
            }
            .onFailure {
                showSnackBar(R.string.store_snack_bar_purchase_failed)
                _uiState.update {
                    it.copy(
                        isPurchasing = false,
                    )
                }
            }
    }

    /**
     * Clear Snack Bar Message
     */
    fun clearSnackBar() {
        _uiState.update {
            it.copy(
                snackBarMessageRes = null,
            )
        }
    }

    /**
     * Set loading ui state as [isLoading]
     */
    private fun setLoading(isLoading: Boolean) {
        _uiState.update {
            it.copy(
                isLoading = isLoading,
            )
        }
    }

    /**
     * Show snack bar message
     */
    private fun showSnackBar(@StringRes msgRes: Int) {
        _uiState.update {
            it.copy(
                snackBarMessageRes = msgRes,
            )
        }
    }

    /**
     * Map [BillingProduct] to [ProductItem]
     */
    private fun BillingProduct.asProductItem(
        isPurchased: Boolean,
    ) = ProductItem(
        title = this.name,
        desc = this.description,
        formattedPrice = this.formattedPrice,
        isPurchased = isPurchased,
    )
}