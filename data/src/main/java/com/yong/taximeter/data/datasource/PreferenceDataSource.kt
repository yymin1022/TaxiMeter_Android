package com.yong.taximeter.data.datasource

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

/**
 * Preference Data Source
 * - Get / Set each preference values
 * - Boolean, Float, Int, String types are supported
 */
class PreferenceDataSource @Inject constructor(
    // Inject Shared Preferences Instance
    private val pref: SharedPreferences,
) {
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