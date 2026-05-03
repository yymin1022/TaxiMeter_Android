package com.yong.taximeter.route.main.subscreen.store.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.taximeter.domain.model.BillingProduct
import com.yong.taximeter.domain.repository.BillingRepository
import com.yong.taximeter.route.main.subscreen.store.model.ProductItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    // Product IDs
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
                    // TODO: Implement failure logic
                }
                .onSuccess {
                    // TODO: Implement success logic
                }

            // Set loading false
            setLoading(false)
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