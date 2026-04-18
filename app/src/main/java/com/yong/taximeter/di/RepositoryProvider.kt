package com.yong.taximeter.di

import com.yong.taximeter.data.dao.CostInfoDao
import com.yong.taximeter.data.datasource.FirestoreDataSource
import com.yong.taximeter.data.datasource.PreferenceDataSource
import com.yong.taximeter.data.repository.CostRepositoryImpl
import com.yong.taximeter.data.repository.SettingRepositoryImpl
import com.yong.taximeter.domain.repository.CostRepository
import com.yong.taximeter.domain.repository.SettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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