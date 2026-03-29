package com.yong.taximeter.route.main.model

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Tab Info
 * - Tabs for Main UI
 */
data class TabInfo(
    val icon: ImageVector,
    @StringRes
    val titleRes: Int,
)
