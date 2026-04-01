package com.yong.taximeter.data.repository

import com.yong.taximeter.domain.repository.PreferenceRepository

/**
 * Preference Repository Implementation
 * - Get / Set each preference values
 * - Boolean, Float, Int, String types are supported
 */
class PreferenceRepositoryImpl: PreferenceRepository {
    /**
     * Getter / Setter for Boolean value
     */
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        // TODO: Implement Getter
        return defaultValue
    }

    override fun setBoolean(key: String, value: Boolean) {
        // TODO: Implement Setter
    }

    /**
     * Getter / Setter for Float value
     */
    override fun getFloat(key: String, defaultValue: Float): Float {
        // TODO: Implement Getter
        return defaultValue
    }

    override fun setFloat(key: String, value: Float) {
        // TODO: Implement Setter
    }

    /**
     * Getter / Setter for Int value
     */
    override fun getInt(key: String, defaultValue: Int): Int {
        // TODO: Implement Getter
        return defaultValue
    }

    override fun setInt(key: String, value: Int) {
        // TODO: Implement Setter
    }

    /**
     * Getter / Setter for String value
     */
    override fun getString(key: String, defaultValue: String): String {
        // TODO: Implement Getter
        return defaultValue
    }

    override fun setString(key: String, value: String) {
        // TODO: Implement Setter
    }
}