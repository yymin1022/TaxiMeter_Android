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
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideSharedPreference(
        @ApplicationContext context: Context,
    ): SharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
}