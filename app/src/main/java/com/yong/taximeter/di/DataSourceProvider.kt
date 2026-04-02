package com.yong.taximeter.di

import android.content.Context
import com.yong.taximeter.data.datasource.PreferenceDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI Provider for Repositories of Domain module
 */
@Module
@InstallIn(SingletonComponent::class)
object DataSourceProvider {
    @Provides
    @Singleton
    fun providePreferenceDataSource(
        @ApplicationContext context: Context,
    ): PreferenceDataSource = PreferenceDataSource(context)
}