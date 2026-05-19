package com.yong.taximeter.service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.yong.taximeter.R
import com.yong.taximeter.domain.model.MeterState
import com.yong.taximeter.domain.model.MeterStatus
import com.yong.taximeter.domain.repository.CostRepository
import com.yong.taximeter.domain.repository.SettingRepository
import com.yong.taximeter.domain.usecase.meter.CalculateMeterCostUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MeterService : Service() {

    inner class MeterBinder : Binder() {
        fun getService(): MeterService = this@MeterService
    }

    private val binder = MeterBinder()
    override fun onBind(intent: Intent): IBinder = binder

    // Inject Repositories
    @Inject
    lateinit var costRepository: CostRepository
    @Inject
    lateinit var settingRepository: SettingRepository

    // Inject Use-case
    @Inject
    lateinit var calculateMeterCostUseCase: CalculateMeterCostUseCase

    // Meter job
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var meterJob: Job? = null

    // City rate state
    private val _isCityRate = MutableStateFlow(false)
    // Night rate state
    private val _isNightRate = MutableStateFlow(false)

    // Meter state instance
    private val _meterState = MutableStateFlow<MeterState?>(null)
    val meterState: StateFlow<MeterState?> = _meterState.asStateFlow()

    /**
     * Start meter service
     * - Ignore if already running
     * - Requires Location permissions
     *
     * @param isCityRate Whether to apply city surcharge
     */
    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ]
    )
    fun startMeter() {
        if(meterJob?.isActive == true) return

        // Run as foreground
        startForeground(NOTIFICATION_ID, createNotification())

        serviceScope.launch {
            // Load cost info
            val regionKey = settingRepository.getCurrentRegion().key
            val costInfo = costRepository.getCostInfo(regionKey) ?: return@launch

            // Init meter calculation use-case
            meterJob = launch {
                calculateMeterCostUseCase(
                    costInfo = costInfo,
                    isCityRate = _isCityRate,
                ).catch {
                    // If any error occurred, update meter state
                    _meterState.value = MeterState(
                        status = MeterStatus.GPS_ERROR,
                    )
                }.collect { state ->
                    // Update meter state
                    _meterState.value = state
                }
            }
        }
    }

    /**
     * Stop meter service
     */
    fun stopMeter() {
        meterJob?.cancel()
        meterJob = null
        _meterState.value = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    /**
     * Update city rate during meter running
     *
     * @param enabled Whether to apply city surcharge
     */
    fun setCityRate(enabled: Boolean) {
        _isCityRate.value = enabled
    }

    /**
     * Update night rate during meter running
     *
     * @param enabled Whether to apply night surcharge
     */
    fun setNightRate(enabled: Boolean) {
        _isNightRate.value = enabled
    }

    /**
     * Stop service scope when destroy
     */
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    /**
     * Create notification instance
     */
    private fun createNotification(): Notification {
        createNotificationChannel()

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_noti_taxi)
            .setContentTitle(getString(R.string.meter_noti_title))
            .setContentText(getString(R.string.meter_noti_content))
            .setOngoing(true)
            .build()
    }

    /**
     * Create notification channel
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.meter_noti_channel_title),
            NotificationManager.IMPORTANCE_LOW,
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "meter_service_channel"
        private const val NOTIFICATION_ID = 1022
    }
}