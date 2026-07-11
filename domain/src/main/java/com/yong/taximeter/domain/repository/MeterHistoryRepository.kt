package com.yong.taximeter.domain.repository

import com.yong.taximeter.domain.model.MeterHistory
import kotlinx.coroutines.flow.Flow

/**
 * Meter History Repository Interface
 */
interface MeterHistoryRepository {
    // Insert new history
    suspend fun insertHistory(history: MeterHistory)

    // Get all histories ordered by timestamp descending
    fun getAllHistories(): Flow<List<MeterHistory>>
}
