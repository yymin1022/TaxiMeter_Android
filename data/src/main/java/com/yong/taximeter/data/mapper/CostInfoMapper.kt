package com.yong.taximeter.data.mapper

import com.yong.taximeter.data.dto.CostInfoItem
import com.yong.taximeter.data.entity.CostInfoEntity

/**
 * Cost Info Mapper
 * - DTO -> Entity
 * - Entity -> Domain Model
 */
object CostInfoMapper {
    /**
     * Map DTO -> Entity
     */
    fun CostInfoItem.toEntity() = CostInfoEntity(
        region = region,
        costBase = data.costBase,
        distBase = data.distBase,
        costRunPer = data.costRunPer,
        costTimePer = data.costTimePer,
        extraRateCity = data.extraRateCity,
        extraRateNight1 = data.extraRateNight1,
        extraRateNight2 = data.extraRateNight2,
        nightStartHour1 = data.nightStartHour1,
        nightStartHour2 = data.nightStartHour2,
        nightEndHour1 = data.nightEndHour1,
        nightEndHour2 = data.nightEndHour2,
    )
}