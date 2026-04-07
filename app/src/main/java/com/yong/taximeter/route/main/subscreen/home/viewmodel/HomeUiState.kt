package com.yong.taximeter.route.main.subscreen.home.viewmodel

import androidx.annotation.StringRes

/**
 * UI State for [HomeViewModel]
 */
data class HomeUiState(
    @get:StringRes
    val snackBarMessageRes: Int? = null,
)