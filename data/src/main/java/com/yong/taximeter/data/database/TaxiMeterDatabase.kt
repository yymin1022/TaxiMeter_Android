package com.yong.taximeter.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yong.taximeter.data.dao.CostInfoDao
import com.yong.taximeter.data.entity.CostInfoEntity

/**
 * TaxiMeter Database based on Room
 */
@Database(
    entities = [CostInfoEntity::class],
    version = 1
)
abstract class TaxiMeterDatabase: RoomDatabase() {
    abstract fun costInfoDao(): CostInfoDao
}