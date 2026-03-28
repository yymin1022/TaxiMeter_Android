package com.yong.taximeter.route.main.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * Main ViewModel
 * - Manages state by [MainUiState]
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    // Inject Dependencies
): ViewModel() {
    // UI State
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
}