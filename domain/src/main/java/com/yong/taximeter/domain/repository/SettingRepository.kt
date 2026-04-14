package com.yong.taximeter.domain.repository

import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.model.RegionSetting
import com.yong.taximeter.domain.model.ThemeSetting


/**
 * Setting Repository Interface
 * - Get region / theme value
 * - Set region / theme value
 */
interface SettingRepository {
    // Custom Cost
    fun setCustomCostInfo(value: CostInfo)

    // Region
    fun getCurrentRegion(): RegionSetting
    fun setRegion(value: RegionSetting)

    // Theme
    fun getCurrentTheme(): ThemeSetting
    fun setTheme(value: ThemeSetting)
}