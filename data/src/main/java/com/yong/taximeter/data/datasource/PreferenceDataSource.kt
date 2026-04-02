package com.yong.taximeter.data.datasource

import android.content.Context
import javax.inject.Inject
import androidx.core.content.edit

/**
 * Preference Data Source
 * - Get / Set each preference values
 * - Boolean, Float, Int, String types are supported
 */
class PreferenceDataSource @Inject constructor(
    // Inject Android Context
    private val context: Context,
) {
    companion object {
        // Default preference name
        private const val DEFAULT_PREF_NAME = "com.yong.taximeter.default_pref"
    }

    // Shared Preferences Instance from Android Context
    private val pref by lazy {
        context.applicationContext.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Getter / Setter for Boolean value
     */
    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return pref.getBoolean(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        pref.edit { putBoolean(key, value) }
    }

    /**
     * Getter / Setter for Float value
     */
    fun getFloat(key: String, defaultValue: Float): Float {
        return pref.getFloat(key, defaultValue)
    }

    fun setFloat(key: String, value: Float) {
        pref.edit { putFloat(key, value) }
    }

    /**
     * Getter / Setter for Int value
     */
    fun getInt(key: String, defaultValue: Int): Int {
        return pref.getInt(key, defaultValue)
    }

    fun setInt(key: String, value: Int) {
        pref.edit { putInt(key, value) }
    }

    /**
     * Getter / Setter for String value
     */
    fun getString(key: String, defaultValue: String): String {
        return pref.getString(key, defaultValue) ?: defaultValue
    }

    fun setString(key: String, value: String) {
        pref.edit { putString(key, value) }
    }
}