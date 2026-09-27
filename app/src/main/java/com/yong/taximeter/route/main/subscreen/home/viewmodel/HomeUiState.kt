package com.yong.taximeter.route.main.subscreen.home.viewmodel

import androidx.annotation.StringRes

/**
 * Caption model for Home Screen
 */
data class HomeCaption(
    @get:StringRes
    val stringRes: Int,
    val formatArgs: List<Any> = emptyList(),
)

/**
 * UI State for [HomeViewModel]
 */
data class HomeUiState(
    @get:StringRes
    val snackBarMessageRes: Int? = null,
    val homeCaption: HomeCaption? = null,
)