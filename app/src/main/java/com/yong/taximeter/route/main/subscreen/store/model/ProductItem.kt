package com.yong.taximeter.route.main.subscreen.store.model

/**
 * Product Item Data Class
 * - Represents UI Item of Each Store Product
 */
data class ProductItem(
    val title: String,
    val desc: String,
    val formattedPrice: String,
    val isPurchased: Boolean,
)