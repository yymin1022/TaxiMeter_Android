package com.yong.taximeter.data.repository

import android.content.Context
import com.yong.taximeter.domain.repository.PreferenceRepository
import javax.inject.Inject
import androidx.core.content.edit

/**
 * Preference Repository Implementation
 * - Get / Set each preference values
 * - Boolean, Float, Int, String types are supported
 */
class PreferenceRepositoryImpl @Inject constructor(
    // Inject Android Context
    private val context: Context,
): PreferenceRepository {
    companion object {
        // Default preference name
        private const val DEFAULT_PREF_NAME = "default_pref"
    }

    // Shared Preferences Instance from Android Context
    private val pref by lazy {
        context.getSharedPreferences(DEFAULT_PREF_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Getter / Setter for Boolean value
     */
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return pref.getBoolean(key, defaultValue)
    }

    override fun setBoolean(key: String, value: Boolean) {
        pref.edit { putBoolean(key, value) }
    }

    /**
     * Getter / Setter for Float value
     */
    override fun getFloat(key: String, defaultValue: Float): Float {
        return pref.getFloat(key, defaultValue)
    }

    override fun setFloat(key: String, value: Float) {
        pref.edit { putFloat(key, value) }
    }

    /**
     * Getter / Setter for Int value
     */
    override fun getInt(key: String, defaultValue: Int): Int {
        return pref.getInt(key, defaultValue)
    }

    override fun setInt(key: String, value: Int) {
        pref.edit { putInt(key, value) }
    }

    /**
     * Getter / Setter for String value
     */
    override fun getString(key: String, defaultValue: String): String {
        return pref.getString(key, defaultValue) ?: defaultValue
    }

    override fun setString(key: String, value: String) {
        pref.edit { putString(key, value) }
    }
}