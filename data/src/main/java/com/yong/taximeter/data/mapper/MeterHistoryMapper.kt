package com.yong.taximeter.data.mapper

import com.yong.taximeter.data.entity.MeterHistoryEntity
import com.yong.taximeter.domain.model.MeterHistory

/**
 * Mapper extension functions between [MeterHistory] and [MeterHistoryEntity]
 */
object MeterHistoryMapper {
    fun MeterHistory.toEntity(): MeterHistoryEntity {
        return MeterHistoryEntity(
            id = id,
            timestamp = timestamp,
            cost = cost,
            distanceMeters = distanceMeters,
            elapsedSeconds = elapsedSeconds,
        )
    }

    fun MeterHistoryEntity.toDomain(): MeterHistory {
        return MeterHistory(
            id = id,
            timestamp = timestamp,
            cost = cost,
            distanceMeters = distanceMeters,
            elapsedSeconds = elapsedSeconds,
        )
    }
}
