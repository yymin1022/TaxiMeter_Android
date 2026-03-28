package com.yong.taximeter.route.meter.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Meter ViewModel
 * - Manages state by [MeterUiState]
 */
@HiltViewModel
class MeterViewModel @Inject constructor(
    // Inject Dependencies
): ViewModel() {
    // UI State
    private val _uiState: MutableStateFlow<MeterUiState> = MutableStateFlow(MeterUiState)
    val uiState: StateFlow<MeterUiState> = _uiState.asStateFlow()
}