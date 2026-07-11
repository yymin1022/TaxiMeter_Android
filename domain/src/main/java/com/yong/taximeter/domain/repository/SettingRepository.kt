package com.yong.taximeter.domain.repository

import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.model.RegionSetting
import com.yong.taximeter.domain.model.ThemeSetting
import com.yong.taximeter.domain.model.ThemeModeSetting
import kotlinx.coroutines.flow.Flow

/**
 * Setting Repository Interface
 * - Get region / theme value
 * - Set region / theme value
 */
interface SettingRepository {
    // Custom Cost
    suspend fun setCustomCostInfo(value: CostInfo)

    // Region
    fun getCurrentRegion(): RegionSetting
    fun setRegion(value: RegionSetting)

    // Theme
    fun getCurrentTheme(): ThemeSetting
    fun setTheme(value: ThemeSetting)

    // Advertisement Removal
    fun isAdRemoved(): Boolean

    // Legal Warning Checked
    fun isLegalWarningChecked(): Boolean
    fun setLegalWarningChecked(checked: Boolean)

    // App Theme Mode
    fun getThemeMode(): ThemeModeSetting
    fun setThemeMode(value: ThemeModeSetting)
    fun observeThemeMode(): Flow<ThemeModeSetting>
}