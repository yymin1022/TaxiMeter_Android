package com.yong.taximeter.di

import android.content.Context
import com.yong.taximeter.data.repository.PreferenceRepositoryImpl
import com.yong.taximeter.domain.repository.PreferenceRepository
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
object RepositoryProvider {
    @Provides
    @Singleton
    fun providePreferenceRepository(
        @ApplicationContext context: Context,
    ): PreferenceRepository = PreferenceRepositoryImpl(context)
}