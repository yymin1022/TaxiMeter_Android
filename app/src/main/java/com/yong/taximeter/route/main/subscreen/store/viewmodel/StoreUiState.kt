package com.yong.taximeter.route.main.subscreen.store.viewmodel

import androidx.annotation.StringRes
import com.yong.taximeter.route.main.subscreen.store.model.ProductItem

/**
 * UI State for [StoreViewModel]
 */
data class StoreUiState(
    // Loading flag
    val isLoading: Boolean = true,

    // Purchase ongoing flag
    val isPurchasing: Boolean = false,

    // Product UI Items
    val productItems: List<ProductItem> = emptyList(),
    // Current selected product index
    val selectedProductIdx: Int? = null,

    // Message resource for SnackBar
    @get:StringRes
    val snackBarMessageRes: Int? = null,
)