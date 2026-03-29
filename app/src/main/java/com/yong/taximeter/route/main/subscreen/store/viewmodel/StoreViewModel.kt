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
    // UI State
    private val _uiState: MutableStateFlow<StoreUiState> = MutableStateFlow(StoreUiState)
    val uiState: StateFlow<StoreUiState> = _uiState.asStateFlow()
}