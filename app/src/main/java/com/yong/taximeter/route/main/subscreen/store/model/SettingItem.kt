package com.yong.taximeter.route.main.subscreen.store.model

import androidx.annotation.StringRes

/**
 * Setting Item Group
 * - Includes title info, and list of [SettingItem]
 */
data class SettingItemGroup(
    @StringRes
    val titleRes: Int? = null,
    val items: List<SettingItem>? = null,
)

/**
 * Setting Item
 */
data class SettingItem(
    @StringRes
    val titleRes: Int? = null,
    @StringRes
    val subtitleRes: Int? = null,
    val isEnabled: Boolean = true,
    val onClick: (() -> Unit)? = null,
)
