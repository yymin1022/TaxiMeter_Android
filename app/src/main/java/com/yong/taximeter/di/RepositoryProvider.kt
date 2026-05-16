package com.yong.taximeter.di

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.yong.taximeter.core.common.AppLogger
import com.yong.taximeter.data.dao.CostInfoDao
import com.yong.taximeter.data.datasource.FirestoreDataSource
import com.yong.taximeter.data.datasource.PreferenceDataSource
import com.yong.taximeter.data.repository.BillingRepositoryImpl
import com.yong.taximeter.data.repository.CostRepositoryImpl
import com.yong.taximeter.data.repository.LocationRepositoryImpl
import com.yong.taximeter.data.repository.SettingRepositoryImpl
import com.yong.taximeter.domain.defs.MeterDefs
import com.yong.taximeter.domain.repository.BillingRepository
import com.yong.taximeter.domain.repository.CostRepository
import com.yong.taximeter.domain.repository.LocationRepository
import com.yong.taximeter.domain.repository.SettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI Provider for Repository Implementation of Data Module
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvider {
    @Provides
    @Singleton
    fun provideBillingRepository(
        @ApplicationContext context: Context,
        logger: AppLogger,
    ): BillingRepository {
        return BillingRepositoryImpl(
            context = context,
            logger = logger,
        )
    }

    @Provides
    @Singleton
    fun provideCostRepository(
        costDao: CostInfoDao,
        firestoreDataSource: FirestoreDataSource,
        preferenceDataSource: PreferenceDataSource,
    ): CostRepository {
        return CostRepositoryImpl(
            costDao,
            firestoreDataSource,
            preferenceDataSource,
        )
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context,
    ): LocationRepository {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            MeterDefs.METER_UPDATE_INTERVAL_MS,
        ).build()

        return LocationRepositoryImpl(
            fusedLocationClient,
            locationRequest,
        )
    }

    @Provides
    @Singleton
    fun provideSettingRepository(
        costDao: CostInfoDao,
        preferenceDataSource: PreferenceDataSource,
    ): SettingRepository {
        return SettingRepositoryImpl(
            costDao,
            preferenceDataSource,
        )
    }
}