package com.yong.taximeter.domain.repository

/**
 * Preference Repository Interface
 */
interface PreferenceRepository {
    fun getBoolean(key: String, defaultValue: Boolean): Boolean
    fun setBoolean(key: String, value: Boolean)

    fun getFloat(key: String, defaultValue: Float): Float
    fun setFloat(key: String, value: Float)

    fun getInt(key: String, defaultValue: Int): Int
    fun setInt(key: String, value: Int)

    fun getString(key: String, defaultValue: String): String
    fun setString(key: String, value: String)
}