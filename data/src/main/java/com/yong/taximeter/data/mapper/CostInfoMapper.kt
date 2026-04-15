package com.yong.taximeter.data.mapper

import com.yong.taximeter.data.dto.CostInfoItem
import com.yong.taximeter.data.entity.CostInfoEntity
import com.yong.taximeter.domain.model.CostInfo

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

    /**
     * Map Entity -> Domain Model
     */
    fun CostInfoEntity.toDomain() = CostInfo(
        region = region,
        costBase = costBase,
        distBase = distBase,
        costRunPer = costRunPer,
        costTimePer = costTimePer,
        extraRateCity = extraRateCity,
        extraRateNight1 = extraRateNight1,
        extraRateNight2 = extraRateNight2,
        nightStartHour1 = nightStartHour1,
        nightStartHour2 = nightStartHour2,
        nightEndHour1 = nightEndHour1,
        nightEndHour2 = nightEndHour2,
    )

    /**
     * Map Model -> Entity
     */
    fun CostInfo.toEntity() = CostInfoEntity(
        region = region,
        costBase = costBase,
        distBase = distBase,
        costRunPer = costRunPer,
        costTimePer = costTimePer,
        extraRateCity = extraRateCity,
        extraRateNight1 = extraRateNight1,
        extraRateNight2 = extraRateNight2,
        nightStartHour1 = nightStartHour1,
        nightStartHour2 = nightStartHour2,
        nightEndHour1 = nightEndHour1,
        nightEndHour2 = nightEndHour2,
        isCustom = true,
    )
}