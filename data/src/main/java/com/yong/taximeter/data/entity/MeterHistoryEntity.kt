package com.yong.taximeter.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Meter History Database Entity Table
 */
@Entity(tableName = "meter_history")
data class MeterHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    @ColumnInfo(name = "cost")
    val cost: Int,
    @ColumnInfo(name = "distance_meters")
    val distanceMeters: Double,
    @ColumnInfo(name = "elapsed_seconds")
    val elapsedSeconds: Double,
)
