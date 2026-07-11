package com.yong.taximeter.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yong.taximeter.data.dao.CostInfoDao
import com.yong.taximeter.data.dao.MeterHistoryDao
import com.yong.taximeter.data.entity.CostInfoEntity
import com.yong.taximeter.data.entity.MeterHistoryEntity

/**
 * TaxiMeter Database based on Room
 */
@Database(
    entities = [
        CostInfoEntity::class,
        MeterHistoryEntity::class,
    ],
    version = 2
)
abstract class TaxiMeterDatabase: RoomDatabase() {
    abstract fun costInfoDao(): CostInfoDao
    abstract fun meterHistoryDao(): MeterHistoryDao
}