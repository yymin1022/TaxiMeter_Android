package com.yong.taximeter.core.logging

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.yong.taximeter.core.common.AppLogger

/**
 * App Logger
 * - Logging, Exception recording is supported
 * - Logging Analytics event is supported
 */
class AppLoggerImpl(
    context: Context,
): AppLogger {
    private val crashlytics = FirebaseCrashlytics.getInstance()
    private val analytics = FirebaseAnalytics.getInstance(context)

    /**
     * Log message as Firebase Crashlytics
     */
    override fun log(
        message: String,
    ) {
        Log.d("TaxiMeter", message)
        crashlytics.log(message)
    }

    /**
     * Record exception to Firebase Crashlytics
     */
    override fun recordException(
        e: Throwable,
        message: String?,
    ) {
        message?.let { crashlytics.log(it) }
        crashlytics.recordException(e)
    }

    /**
     * Log event to Firebase Analytics
     */
    override fun logEvent(
        name: String,
        params: Map<String, Any>?,
    ) {
        val bundle = Bundle().apply {
            params?.forEach { (key, value) ->
                when(value) {
                    is Boolean -> putBoolean(key, value)
                    is Double -> putDouble(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is String -> putString(key, value)
                }
            }
        }
        analytics.logEvent(name, bundle)
    }
}