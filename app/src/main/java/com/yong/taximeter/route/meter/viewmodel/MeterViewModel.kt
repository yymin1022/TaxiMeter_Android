package com.yong.taximeter.route.meter.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.annotation.RequiresPermission
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yong.taximeter.R
import com.yong.taximeter.domain.model.MeterStatus
import com.yong.taximeter.domain.model.ThemeSetting
import com.yong.taximeter.domain.repository.SettingRepository
import com.yong.taximeter.service.MeterService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Meter ViewModel
 * - Manages state by [MeterUiState]
 */
@HiltViewModel
class MeterViewModel @Inject constructor(
    // Inject Android Context
    @ApplicationContext private val context: Context,
    // Inject Setting Repository
    private val settingRepository: SettingRepository,
) : ViewModel() {
    // UI State
    private val _uiState: MutableStateFlow<MeterUiState> = MutableStateFlow(MeterUiState())
    val uiState: StateFlow<MeterUiState> = _uiState.asStateFlow()

    // Meter service instance
    @SuppressLint("StaticFieldLeak")
    private var meterService: MeterService? = null
    private var observeJob: Job? = null

    // Current meter status
    private var curMeterStatus: MeterStatus? = null

    // Service connection callback
    private val serviceConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            // Load as MeterService Binder
            val service = (binder as MeterService.MeterBinder).getService()
            meterService = service

            // Cancel any previous observing
            observeJob?.cancel()

            // Observe meter state from service
            observeJob = viewModelScope.launch {
                service.meterState.collect { state ->
                    _uiState.update {
                        if(state == null) {
                            curMeterStatus = MeterStatus.NOT_RUNNING
                            it.copy(
                                meterStatus = curMeterStatus!!,
                            )
                        } else {
                            curMeterStatus = state.status
                            it.copy(
                                currentCost = state.currentCost,
                                costCounter = state.costCounter,
                                currentSpeedKph = state.currentSpeedKph,
                                totalDistanceMeters = state.totalDistanceMeters,
                                meterStatus = curMeterStatus!!,
                                isNightRate = state.isNightRate,
                                isCityRate = state.isCityRate,
                            )
                        }
                    }
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            meterService = null
            observeJob?.cancel()
        }
    }

    init {
        bindMeterService()
        loadAnimationFrames()
        loadAdRemovalStatus()
        checkLegalWarning()
    }

    /**
     * On confirm legal warning dialog
     */
    fun onConfirmLegalWarning() {
        settingRepository.setLegalWarningChecked(true)
        _uiState.update {
            it.copy(
                showLegalWarningDialog = false,
            )
        }
    }

    /**
     * On dismiss legal warning dialog
     */
    fun onDismissLegalWarning() {
        _uiState.update {
            it.copy(
                showLegalWarningDialog = false,
            )
        }
    }

    /**
     * Check if legal warning is already checked, and show dialog if not
     */
    private fun checkLegalWarning() {
        val checked = settingRepository.isLegalWarningChecked()
        if(!checked) {
            _uiState.update {
                it.copy(
                    showLegalWarningDialog = true,
                )
            }
        }
    }

    /**
     * Load ad removal status from setting repository
     */
    private fun loadAdRemovalStatus() {
        val isAdRemoved = settingRepository.isAdRemoved()
        _uiState.update {
            it.copy(
                isAdRemoved = isAdRemoved,
            )
        }
    }

    /**
     * Bind to [MeterService] with [serviceConnection]
     */
    private fun bindMeterService() {
        Intent(context, MeterService::class.java).also { intent ->
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }
    }

    /**
     * Load animation frames based on current theme setting
     */
    private fun loadAnimationFrames() {
        // Get current theme setting
        val themeSetting = settingRepository.getCurrentTheme()
        // Generate animation frames list
        val animationFrames = when(themeSetting) {
            ThemeSetting.HORSE -> listOf(
                R.drawable.ic_horse_1,
                R.drawable.ic_horse_2,
                R.drawable.ic_horse_3,
            )
            ThemeSetting.CIRCLE -> listOf(
                R.drawable.ic_circle_1,
                R.drawable.ic_circle_2,
                R.drawable.ic_circle_3,
                R.drawable.ic_circle_4,
                R.drawable.ic_circle_5,
                R.drawable.ic_circle_6,
                R.drawable.ic_circle_7,
                R.drawable.ic_circle_8,
            )
        }

        // Update UI State
        _uiState.update {
            it.copy(
                animationFrames = animationFrames,
            )
        }
    }

    /**
     * On click start meter button
     * - Caller must ensure location permission is granted before calling
     */
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onClickStart() {
        meterService?.startMeter()
    }

    /**
     * On click stop meter button
     * - Show stop confirm dialog if meter is running
     */
    fun onClickStop() {
        if(curMeterStatus == null
            || curMeterStatus == MeterStatus.NOT_RUNNING) return

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    showStopDialog = true,
                )
            }
        }
    }

    /**
     * On cancel stop meter dialog
     */
    fun onCancelStop() {
        _uiState.update {
            it.copy(
                showStopDialog = false,
            )
        }
    }

    /**
     * On confirm stop meter dialog
     */
    fun onConfirmStop() {
        curMeterStatus = null
        meterService?.stopMeter()
        _uiState.update { MeterUiState() }
    }

    /**
     * On click city rate
     */
    fun onClickCityRate() {
        // Toggle city rate state
        val newValue = _uiState.value.isCityRate.not()
        meterService?.setCityRate(newValue)
        _uiState.update { it.copy(isCityRate = newValue) }
    }

    /**
     * On click night rate
     */
    fun onClickNightRate() {
        // Show SnackBar
        showSnackBar(R.string.meter_snack_bar_night_rate_info)
    }

    /**
     * Close service binding / observing when clear
     */
    override fun onCleared() {
        super.onCleared()
        observeJob?.cancel()
        context.unbindService(serviceConnection)
        meterService = null
    }

    /**
     * Clear Snack Bar Message
     */
    fun clearSnackBar() {
        _uiState.update {
            it.copy(
                snackBarMessageRes = null,
            )
        }
    }

    /**
     * Show snack bar message
     */
    private fun showSnackBar(@StringRes msgRes: Int) {
        _uiState.update {
            it.copy(
                snackBarMessageRes = msgRes,
            )
        }
    }
}