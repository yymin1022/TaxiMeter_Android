package com.yong.taximeter.data.dto

import com.google.firebase.firestore.PropertyName

/**
 * Cost Version DTO
 * - Firebase Firestore -> DTO
 */
data class CostVersionDto(
    @get:PropertyName("data")
    @PropertyName("data")
    val version: String = "",
)
