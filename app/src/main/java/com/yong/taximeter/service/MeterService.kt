package com.yong.taximeter.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import com.yong.taximeter.R
import com.yong.taximeter.core.common.AppLogger
import com.yong.taximeter.domain.model.MeterHistory
import com.yong.taximeter.domain.model.MeterState
import com.yong.taximeter.domain.model.MeterStatus
import com.yong.taximeter.domain.repository.CostRepository
import com.yong.taximeter.domain.repository.MeterHistoryRepository
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
import androidx.core.net.toUri

@AndroidEntryPoint
class MeterService : Service() {

    inner class MeterBinder : Binder() {
        fun getService(): MeterService = this@MeterService
    }

    private val binder = MeterBinder()
    override fun onBind(intent: Intent): IBinder = binder

    @SuppressLint("MissingPermission")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val hasFineLocation = androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED
                val hasCoarseLocation = androidx.core.content.ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                if (hasFineLocation || hasCoarseLocation) {
                    startMeter()
                } else {
                    stopSelf()
                }
            }
            ACTION_STOP -> {
                stopMeter()
            }
        }
        return START_NOT_STICKY
    }

    // Inject Repositories
    @Inject
    lateinit var costRepository: CostRepository
    @Inject
    lateinit var settingRepository: SettingRepository
    @Inject
    lateinit var meterHistoryRepository: MeterHistoryRepository
    @Inject
    lateinit var logger: AppLogger

    // Inject Use-case
    @Inject
    lateinit var calculateMeterCostUseCase: CalculateMeterCostUseCase

    // Meter job
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var meterJob: Job? = null

    // City rate state
    private val _isCityRate = MutableStateFlow(false)

    // Meter state instance
    private val _meterState = MutableStateFlow<MeterState?>(null)
    val meterState: StateFlow<MeterState?> = _meterState.asStateFlow()

    // Reusable notification builder
    private var notificationBuilder: NotificationCompat.Builder? = null

    /**
     * Start meter service
     * - Ignore if already running
     * - Requires Location permissions
     */
    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ]
    )
    fun startMeter() {
        if(meterJob?.isActive == true) return

        // Reset city rate status for a new run
        _isCityRate.value = false

        // Run as foreground
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                createNotification(),
                android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION
            )
        } else {
            startForeground(NOTIFICATION_ID, createNotification())
        }

        serviceScope.launch {
            // Load cost info
            val regionKey = settingRepository.getCurrentRegion().key
            val costInfo = costRepository.getCostInfo(regionKey)

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
                    // Update notification with current state
                    updateNotification(state)
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

        val state = _meterState.value
        if (state != null && (state.currentCost > 0 || state.totalDistanceMeters > 0.0)) {
            serviceScope.launch {
                try {
                    meterHistoryRepository.insertHistory(
                        MeterHistory(
                            timestamp = System.currentTimeMillis(),
                            cost = state.currentCost,
                            distanceMeters = state.totalDistanceMeters,
                            elapsedSeconds = state.totalElapsedSeconds,
                        )
                    )
                } catch (e: Exception) {
                    logger.recordException(e, "Failed to insert meter history")
                } finally {
                    cleanupService()
                }
            }
        } else {
            cleanupService()
        }
    }

    private fun cleanupService() {
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
     * Stop service scope when destroy
     */
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        notificationBuilder = null
    }

    /**
     * Create notification instance
     */
    private fun createNotification(
        state: MeterState? = null,
    ): Notification {
        if (notificationBuilder == null) {
            createNotificationChannel()
            notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_noti_taxi)
                .setContentTitle(getString(R.string.meter_noti_title))
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOnlyAlertOnce(true)
                .setRequestPromotedOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
        }

        // Notification content text
        // - Cost if state is available, or default string if not
        val contentText = if (state != null) {
            val costText = getString(R.string.meter_cost, java.text.NumberFormat.getNumberInstance().format(state.currentCost))
            val distanceText = getString(R.string.meter_info_distance_data, state.totalDistanceMeters / 1000)
            "$costText / $distanceText"
        } else {
            getString(R.string.meter_noti_content)
        }

        val shortText = if (state != null) {
            getString(R.string.meter_cost, java.text.NumberFormat.getNumberInstance().format(state.currentCost))
        } else {
            getString(R.string.meter_cost, "0")
        }

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(contentText)

        val builder = notificationBuilder!!
            .setContentText(contentText)
            .setShortCriticalText(shortText)
            .setStyle(bigTextStyle)

        return builder.build()
    }

    /**
     * Apply to notification manager
     */
    private fun updateNotification(state: MeterState) {
        val manager = getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, createNotification(state))
    }

    /**
     * Create notification channel
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            getString(R.string.meter_noti_channel_title),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            // Samsung One UI collapses notifications if sound is null (Silent).
            // To keep it in the "Alerting" section (not collapsed) but remain silent,
            // we set the sound to a non-existent dummy resource URI.
            // The system fails to resolve the resource and plays nothing, while maintaining the "Alerting" status.
            val dummyUri = "android.resource://$packageName/raw/silent".toUri()
            val audioAttributes = android.media.AudioAttributes.Builder()
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION)
                .build()
            setSound(dummyUri, audioAttributes)
            enableVibration(false)
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val ACTION_START = "com.yong.taximeter.ACTION_START"
        const val ACTION_STOP = "com.yong.taximeter.ACTION_STOP"
        private const val NOTIFICATION_CHANNEL_ID = "meter_service_channel_v2"
        private const val NOTIFICATION_ID = 1022
    }
}