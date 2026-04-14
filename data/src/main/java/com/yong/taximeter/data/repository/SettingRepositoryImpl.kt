package com.yong.taximeter.data.repository

import com.yong.taximeter.data.datasource.PreferenceDataSource
import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.model.RegionSetting
import com.yong.taximeter.domain.model.ThemeSetting
import com.yong.taximeter.domain.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    // Inject Preference DataSource
    private val preferenceDataSource: PreferenceDataSource,
): SettingRepository {
    companion object {
        private const val PREF_KEY_SETTING_REGION = "PREF_KEY_SETTING_REGION"
        private const val PREF_KEY_SETTING_THEME = "PREF_KEY_SETTING_THEME"

        private val DEFAULT_REGION = RegionSetting.SEOUL
        private val DEFAULT_THEME = ThemeSetting.HORSE
    }

    override fun setCustomCostInfo(value: CostInfo) {
        // TODO: Implement
    }

    override fun getCurrentRegion(): RegionSetting {
        val regionKey = preferenceDataSource.getString(PREF_KEY_SETTING_REGION, DEFAULT_REGION.key)
        return RegionSetting.entries.find { it.key == regionKey } ?: DEFAULT_REGION
    }

    override fun setRegion(value: RegionSetting) {
        preferenceDataSource.setString(PREF_KEY_SETTING_REGION, value.key)
    }

    override fun getCurrentTheme(): ThemeSetting {
        val themeKey = preferenceDataSource.getString(PREF_KEY_SETTING_THEME, DEFAULT_THEME.key)
        return ThemeSetting.entries.find { it.key == themeKey } ?: DEFAULT_THEME
    }

    override fun setTheme(value: ThemeSetting) {
        preferenceDataSource.setString(PREF_KEY_SETTING_THEME, value.key)
    }
}