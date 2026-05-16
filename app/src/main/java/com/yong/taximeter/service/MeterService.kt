package com.yong.taximeter.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeterService: Service() {
    inner class MeterBinder: Binder() {
        fun getService(): MeterService = this@MeterService
    }

    private val binder = MeterBinder()
    override fun onBind(intent: Intent): IBinder = binder
}