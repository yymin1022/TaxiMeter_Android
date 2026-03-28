package com.yong.taximeter.di

import android.content.Context
import com.yong.taximeter.core.common.AppLogger
import com.yong.taximeter.core.logging.AppLoggerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI Provider for [AppLogger]
 */
@Module
@InstallIn(SingletonComponent::class)
object LoggerProvider {
    @Provides
    @Singleton
    fun provideAppLogger(
        @ApplicationContext context: Context,
    ): AppLogger = AppLoggerImpl(context)
}