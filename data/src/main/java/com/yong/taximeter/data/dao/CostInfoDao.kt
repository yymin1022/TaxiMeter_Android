package com.yong.taximeter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.yong.taximeter.data.entity.CostInfoEntity

/**
 * Cost Info DAO
 */
@Dao
interface CostInfoDao {
    @Query("SELECT * FROM cost_info WHERE region = :regionKey")
    fun getByRegion(regionKey: String): CostInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(costs: List<CostInfoEntity>)

    @Query("DELETE FROM cost_info WHERE isCustom = 0")
    suspend fun deleteAllRemoteCost()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomCost(cost: CostInfoEntity)

    @Transaction
    suspend fun updateRemoteCost(costs: List<CostInfoEntity>) {
        deleteAllRemoteCost()
        insertAll(costs)
    }
}