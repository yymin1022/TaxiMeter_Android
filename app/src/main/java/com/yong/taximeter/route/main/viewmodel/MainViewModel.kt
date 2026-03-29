package com.yong.taximeter.route.main.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.lifecycle.ViewModel
import com.yong.taximeter.R
import com.yong.taximeter.route.main.model.TabInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Main ViewModel
 * - Manages state by [MainUiState]
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    // Inject Dependencies
): ViewModel() {
    companion object {
        private const val INIT_SELECTED_TAB_IDX = 1
    }

    // UI State
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /**
     * Initialize Tab Info
     */
    fun initTabInfo() {
        // Generate Tab List
        val tabList = buildList {
            // 0. Setting Tab
            add(
                TabInfo(
                    icon = Icons.Outlined.Settings,
                    titleRes = R.string.main_tab_setting,
                ))

            // 1. Home Tab
            add(
                TabInfo(
                    icon = Icons.Outlined.Home,
                    titleRes = R.string.main_tab_home,
                ))

            // 2. Store Tab
            add(
                TabInfo(
                    icon = Icons.Outlined.ShoppingCart,
                    titleRes = R.string.main_tab_store,
                ))
        }

        // Update UI State
        _uiState.update {
            it.copy(
                selectedTabIdx = INIT_SELECTED_TAB_IDX,
                tabList = tabList,
            )
        }
    }

    /**
     * On select tab
     */
    fun selectTab(idx: Int) {
        // Update UI State
        _uiState.update {
            it.copy(
                selectedTabIdx = idx,
            )
        }
    }
}