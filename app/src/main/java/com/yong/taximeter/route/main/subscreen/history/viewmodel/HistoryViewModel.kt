package com.yong.taximeter.route.main.subscreen.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.taximeter.domain.repository.MeterHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val meterHistoryRepository: MeterHistoryRepository,
): ViewModel() {
    // UI State
    private val _uiState: MutableStateFlow<HistoryUiState> = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistories()
    }

    /**
     * Load driving histories from repository
     */
    private fun loadHistories() {
        viewModelScope.launch {
            meterHistoryRepository.getAllHistories().collect { histories ->
                val totalCount = histories.size
                val totalCost = histories.sumOf { it.cost.toLong() }
                val totalDistance = histories.sumOf { it.distanceMeters }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        histories = histories,
                        totalCount = totalCount,
                        totalCost = totalCost,
                        totalDistanceMeters = totalDistance,
                    )
                }
            }
        }
    }

    /**
     * Delete single driving history by ID
     */
    fun deleteHistory(id: Long) {
        viewModelScope.launch {
            meterHistoryRepository.deleteHistory(id)
        }
    }

    /**
     * Delete all driving histories
     */
    fun deleteAllHistories() {
        viewModelScope.launch {
            meterHistoryRepository.deleteAllHistories()
            hideClearAllConfirm()
        }
    }

    /**
     * Show clear all confirm dialog
     */
    fun showClearAllConfirm() {
        _uiState.update {
            it.copy(showClearAllConfirm = true)
        }
    }

    /**
     * Hide clear all confirm dialog
     */
    fun hideClearAllConfirm() {
        _uiState.update {
            it.copy(showClearAllConfirm = false)
        }
    }
}
