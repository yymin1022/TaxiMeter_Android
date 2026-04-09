package com.yong.taximeter.route.main.subscreen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.taximeter.R
import com.yong.taximeter.domain.usecase.cost.UpdateCostInfoResult
import com.yong.taximeter.domain.usecase.cost.UpdateCostInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    // Inject Dependencies
    private val updateCostInfoUseCase: UpdateCostInfoUseCase,
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

    /**
     * Check and apply cost info update
     */
    fun updateCostInfo() {
        viewModelScope.launch {
            // Invoke update
            val updateResult = updateCostInfoUseCase()

            // Get result message
            val messageRes = when(updateResult) {
                UpdateCostInfoResult.CANCELED,
                UpdateCostInfoResult.UP_TO_DATE -> null
                UpdateCostInfoResult.SUCCESS -> R.string.home_snack_bar_update_success
                UpdateCostInfoResult.FAILURE -> R.string.home_snack_bar_update_failure
            }

            // Show message as snack bar
            _uiState.update {
                it.copy(
                    snackBarMessageRes = messageRes,
                )
            }
        }
    }
}