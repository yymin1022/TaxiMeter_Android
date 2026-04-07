package com.yong.taximeter.route.main.subscreen.home.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    // Inject Dependencies
): ViewModel() {
    // UI State
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

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
}