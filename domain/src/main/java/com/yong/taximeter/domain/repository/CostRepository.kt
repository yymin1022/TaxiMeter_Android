package com.yong.taximeter.domain.repository

import com.yong.taximeter.domain.model.CostInfo

/**
 * Cost Repository Interface
 * - Get cost info for each region
 * - Update to latest cost info
 */
interface CostRepository {
    // Get Local / Remote cost version
    fun getLocalVersion(): String
    suspend fun getRemoteVersion(): String

    // Update local cost info to latest version
    fun updateToLatest(): Boolean

    // Get cost info for specific region
    fun getCostInfo(regionKey: String): CostInfo?
}