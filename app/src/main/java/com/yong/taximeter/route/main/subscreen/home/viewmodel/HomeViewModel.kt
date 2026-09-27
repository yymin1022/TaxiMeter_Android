package com.yong.taximeter.route.main.subscreen.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.taximeter.R
import com.yong.taximeter.domain.repository.MeterHistoryRepository
import com.yong.taximeter.domain.usecase.cost.UpdateCostInfoResult
import com.yong.taximeter.domain.usecase.cost.UpdateCostInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    // Inject Dependencies
    private val meterHistoryRepository: MeterHistoryRepository,
    private val updateCostInfoUseCase: UpdateCostInfoUseCase,
): ViewModel() {
    companion object {
        private const val MIN_HISTORY_COUNT_FOR_STATS = 3
    }

    // UI State
    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHistoryCaption()
    }

    /**
     * Load driving histories and select random home caption
     */
    private fun loadHistoryCaption() {
        viewModelScope.launch {
            meterHistoryRepository.getAllHistories().collect { histories ->
                val totalCount = histories.size
                val totalCost = histories.sumOf { it.cost.toLong() }
                val totalDistanceKm = histories.sumOf { it.distanceMeters } / 1000f

                val candidates = mutableListOf<HomeCaption>()

                // 1. Safe drive (Always)
                candidates.add(
                    HomeCaption(
                        stringRes = R.string.home_caption_safe_drive,
                    )
                )

                // 2. Statistics captions (When totalCount >= 3)
                if (totalCount >= MIN_HISTORY_COUNT_FOR_STATS) {
                    // Distance
                    candidates.add(
                        HomeCaption(
                            stringRes = R.string.home_caption_distance,
                            formatArgs = listOf(totalDistanceKm),
                        )
                    )

                    // Cost
                    val formattedCost = NumberFormat.getNumberInstance().format(totalCost)
                    candidates.add(
                        HomeCaption(
                            stringRes = R.string.home_caption_cost,
                            formatArgs = listOf(formattedCost),
                        )
                    )

                    // Trip count
                    candidates.add(
                        HomeCaption(
                            stringRes = R.string.home_caption_trips,
                            formatArgs = listOf(totalCount + 1),
                        )
                    )
                }

                val selectedCaption = candidates.random()
                _uiState.update {
                    it.copy(homeCaption = selectedCaption)
                }
            }
        }
    }

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