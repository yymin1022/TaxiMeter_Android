package com.yong.taximeter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yong.taximeter.data.entity.MeterHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room Data Access Object for [MeterHistoryEntity]
 */
@Dao
interface MeterHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(entity: MeterHistoryEntity)

    @Query("SELECT * FROM meter_history ORDER BY timestamp DESC")
    fun getAllHistories(): Flow<List<MeterHistoryEntity>>
}
