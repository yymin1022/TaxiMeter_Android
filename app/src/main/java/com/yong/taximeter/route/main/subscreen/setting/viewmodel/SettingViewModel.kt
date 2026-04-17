package com.yong.taximeter.route.main.subscreen.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.taximeter.R
import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.model.RegionSetting
import com.yong.taximeter.domain.model.ThemeSetting
import com.yong.taximeter.domain.repository.CostRepository
import com.yong.taximeter.domain.repository.SettingRepository
import com.yong.taximeter.route.main.subscreen.setting.model.SettingItem
import com.yong.taximeter.route.main.subscreen.setting.model.SettingItemGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    // Inject Cost Repository
    private val costRepository: CostRepository,
    // Inject Setting Repository
    private val settingRepository: SettingRepository,
): ViewModel() {
    companion object {
        private const val URL_DEVELOPER_BLOG = "https://dev-lr.com"
        private const val URL_DEVELOPER_GITHUB = "https://github.com/yymin1022"
        private const val URL_DEVELOPER_LINKEDIN = "https://linkedin.com/in/yymin1022"
        private const val URL_PRIVACY_POLICY = "https://defcon.or.kr/privacy"
    }

    // UI State
    private val _uiState: MutableStateFlow<SettingUiState> = MutableStateFlow(SettingUiState())
    val uiState: StateFlow<SettingUiState> = _uiState.asStateFlow()

    /**
     * Load all setting groups
     */
    fun loadSettingGroups() {
        viewModelScope.launch {
            // Cost Info setting group
            val costInfoSettingGroup = loadCostInfoSettingGroup()
            // Developer Info setting group
            val developerInfoSettingGroup = loadDeveloperInfoSettingGroup()
            // Meter setting group
            val meterSettingGroup = loadMeterSettingGroup()

            // Build Setting Groups
            val settingGroups = buildList {
                add(meterSettingGroup)
                add(costInfoSettingGroup)
                add(developerInfoSettingGroup)
            }

            // Update UI State
            _uiState.update {
                it.copy(
                    settingGroups = settingGroups,
                )
            }
        }
    }

    /**
     * Dismiss any dialog
     */
    fun dismissDialog() {
        _uiState.update {
            it.copy(
                showDialog = ShowDialog.Nothing,
            )
        }
    }

    /**
     * Load cost info setting group
     */
    private fun loadCostInfoSettingGroup(): SettingItemGroup {
        return SettingItemGroup(
            titleRes = R.string.setting_group_title_cost_info,
            items = buildList {
                // Cost Info item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_cost_info,
                        subtitleRes = R.string.setting_item_subtitle_cost_info_night_1step,
                    )
                )

                // Custom cost flag
                val isCustomCost = (settingRepository.getCurrentRegion()== RegionSetting.CUSTOM)
                // Set custom cost item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_meter_set_custom_cost,
                        subtitleRes =
                            if(isCustomCost) R.string.setting_item_subtitle_meter_set_custom_cost_enabled
                            else R.string.setting_item_subtitle_meter_set_custom_cost_disabled,
                        isEnabled = isCustomCost,
                        onClick = this@SettingViewModel::onClickCustomCostSettingItem,
                    )
                )

                // Cost DB Version
                val curCostVersionText = costRepository.getLocalVersion()
                // Cost item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_cost_version,
                        subtitle = curCostVersionText,
                    )
                )
            }
        )
    }

    /**
     * Load developer info setting group
     */
    private fun loadDeveloperInfoSettingGroup(): SettingItemGroup {
        return SettingItemGroup(
            titleRes = R.string.setting_group_title_developer_info,
            items = buildList {
                // Developer name item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_developer_name,
                        subtitleRes = R.string.setting_item_subtitle_developer_name,
                    )
                )

                // Developer blog item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_developer_blog,
                        subtitle = URL_DEVELOPER_BLOG,
                    )
                )

                // Developer github item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_developer_github,
                        subtitle = URL_DEVELOPER_GITHUB,
                    )
                )

                // Developer linkedin item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_developer_linkedin,
                        subtitle = URL_DEVELOPER_LINKEDIN,
                    )
                )

                // Privacy policy item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_privacy_policy,
                        subtitle = URL_PRIVACY_POLICY,
                    )
                )
            }
        )
    }

    /**
     * Load meter setting group
     */
    private fun loadMeterSettingGroup(): SettingItemGroup {
        return SettingItemGroup(
            titleRes = R.string.setting_group_title_meter,
            items = buildList {
                // Regin value
                val curRegionTextRes = settingRepository.getCurrentRegion().toStringRes()
                // Region item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_meter_region,
                        subtitleRes = curRegionTextRes,
                        onClick = this@SettingViewModel::onClickRegionSettingItem,
                    )
                )

                // Theme value
                val curThemeTextRes = settingRepository.getCurrentTheme().toStringRes()
                // Theme item
                add(
                    SettingItem(
                        titleRes = R.string.setting_item_title_meter_theme,
                        subtitleRes = curThemeTextRes,
                        onClick = this@SettingViewModel::onClickThemeSettingItem,
                    )
                )
            }
        )
    }

    /**
     * Click CustomCost Setting Item
     */
    private fun onClickCustomCostSettingItem() {
        // CustomCost Dialog State
        val showCustomCostDialog = ShowDialog.CustomCostDialog(
            titleRes = R.string.setting_dialog_custom_cost_title,
            onComplete = this::onCompleteCustomCostSetting,
        )

        // Show Region Setting Dialog
        _uiState.update {
            it.copy(
                showDialog = showCustomCostDialog,
            )
        }
    }

    /**
     * Complete CustomCost Setting
     */
    private fun onCompleteCustomCostSetting(costInfo: CostInfo) {
        viewModelScope.launch {
            // Set Cost Info
            settingRepository.setCustomCostInfo(costInfo)

            // Reload Setting Items
            loadSettingGroups()

            // Close Dialog
            _uiState.update {
                it.copy(
                    showDialog = ShowDialog.Nothing,
                )
            }
        }
    }

    /**
     * Click Region Setting Item
     */
    private fun onClickRegionSettingItem() {
        // Region Select Dialog State
        val showRegionSelectDialog = ShowDialog.RadioSelectDialog(
            titleRes = R.string.setting_dialog_region_setting_title,
            itemTextResources = RegionSetting.entries.map { it.toStringRes() },
            onComplete = this::onCompleteRegionSetting,
        )

        // Show Region Setting Dialog
        _uiState.update {
            it.copy(
                showDialog = showRegionSelectDialog,
            )
        }
    }

    /**
     * Selected Region Setting
     */
    private fun onCompleteRegionSetting(idx: Int) {
        // Get selected theme
        val selectedTheme = RegionSetting.entries.get(idx)
        // Update setting
        settingRepository.setRegion(selectedTheme)

        // Reload Setting Items
        loadSettingGroups()

        // Close Dialog
        _uiState.update {
            it.copy(
                showDialog = ShowDialog.Nothing,
            )
        }
    }

    /**
     * Click Theme Setting Item
     */
    private fun onClickThemeSettingItem() {
        // Theme Select Dialog State
        val showThemeSelectDialog = ShowDialog.RadioSelectDialog(
            titleRes = R.string.setting_dialog_theme_setting_title,
            itemTextResources = ThemeSetting.entries.map { it.toStringRes() },
            onComplete = this::onCompleteThemeSetting,
        )

        // Show Region Setting Dialog
        _uiState.update {
            it.copy(
                showDialog = showThemeSelectDialog,
            )
        }
    }

    /**
     * Selected Theme Setting
     */
    private fun onCompleteThemeSetting(idx: Int) {
        // Get selected theme
        val selectedTheme = ThemeSetting.entries.get(idx)
        // Update setting
        settingRepository.setTheme(selectedTheme)

        // Reload Setting Items
        loadSettingGroups()

        // Close Dialog
        _uiState.update {
            it.copy(
                showDialog = ShowDialog.Nothing,
            )
        }
    }

    /**
     * Convert [RegionSetting] to String Resource
     */
    private fun RegionSetting.toStringRes(): Int {
        return when(this) {
            RegionSetting.SEOUL -> R.string.region_seoul
            RegionSetting.GANGWON -> R.string.region_gangwon
            RegionSetting.GYEONGGI -> R.string.region_gyeonggi
            RegionSetting.GYEONGBUK -> R.string.region_gyeongbuk
            RegionSetting.GYEONGNAM -> R.string.region_gyeongnam
            RegionSetting.GWANGJU -> R.string.region_gwangju
            RegionSetting.DAEGU -> R.string.region_daegu
            RegionSetting.DAEJEON -> R.string.region_daejeon
            RegionSetting.BUSAN -> R.string.region_busan
            RegionSetting.ULSAN -> R.string.region_ulsan
            RegionSetting.INCHEON -> R.string.region_incheon
            RegionSetting.JEONBUK -> R.string.region_jeonbuk
            RegionSetting.JEONNAM -> R.string.region_jeonnam
            RegionSetting.JEJU -> R.string.region_jeju
            RegionSetting.CHUNGBUK -> R.string.region_chungbuk
            RegionSetting.CHUNGNAM -> R.string.region_chungnam
            RegionSetting.CUSTOM -> R.string.region_custom
        }
    }

    /**
     * Convert [ThemeSetting] to String Resource
     */
    private fun ThemeSetting.toStringRes(): Int {
        return when(this) {
            ThemeSetting.CIRCLE -> R.string.theme_circle
            ThemeSetting.HORSE -> R.string.theme_horse
        }
    }
}