package com.yong.taximeter.data.repository

import com.yong.taximeter.domain.model.RegionSetting
import com.yong.taximeter.domain.model.ThemeSetting
import com.yong.taximeter.domain.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    // Inject dependencies
): SettingRepository {
    override fun getCurrentRegion(): RegionSetting {
        // TODO
    }

    override fun setRegion(value: RegionSetting): Boolean {
        // TODO
    }

    override fun getCurrentTheme(): ThemeSetting {
        // TODO
    }

    override fun setTheme(value: ThemeSetting): Boolean {
        // TODO
    }
}