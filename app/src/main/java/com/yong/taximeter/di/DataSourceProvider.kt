package com.yong.taximeter.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI Provider for Data Source of Data module
 */
@Module
@InstallIn(SingletonComponent::class)
object DataSourceProvider {
    // Shared Preferences Key
    private const val SHARED_PREF_DEFAULT_KEY = "com.yong.taximeter.pref"

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideSharedPreference(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(SHARED_PREF_DEFAULT_KEY, Context.MODE_PRIVATE)
}