package com.yong.taximeter.route.main.subscreen.history.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HistoryViewModel @Inject constructor(
    // Inject Dependencies
): ViewModel() {
    // UI State
    private val _uiState: MutableStateFlow<HistoryUiState> = MutableStateFlow(HistoryUiState)
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
}
