package com.yong.taximeter.core.common

/**
 * App Logger Interface
 * - Logging, Exception recording is supported
 * - Logging Analytics event is supported
 */
interface AppLogger {
    // Crash / Debug Logging
    fun log(message: String)
    fun recordException(e: Throwable, message: String? = null)

    // Analytics Logging
    fun logEvent(name: String, params: Map<String, Any>? = null)
}