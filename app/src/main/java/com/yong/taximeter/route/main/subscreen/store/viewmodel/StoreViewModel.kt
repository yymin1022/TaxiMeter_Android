package com.yong.taximeter.route.main.subscreen.store.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    // Inject Dependencies
): ViewModel() {
    companion object {
        // Product SKU IDs
        private const val SKU_REMOVE_ADVERTISEMENT = "ad_remove"
        private const val SKU_DONATE_1000 = "donate_1000"
        private const val SKU_DONATE_5000 = "donate_5000"
        private const val SKU_DONATE_10000 = "donate_10000"
        private const val SKU_DONATE_50000 = "donate_50000"
    }

    // UI State
    private val _uiState: MutableStateFlow<StoreUiState> = MutableStateFlow(StoreUiState())
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()
}