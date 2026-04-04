package com.yong.taximeter.di

import android.content.Context
import androidx.room.Room
import com.yong.taximeter.data.dao.CostInfoDao
import com.yong.taximeter.data.database.TaxiMeterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt DI Provider for Room Database of Data module
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseProvider {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): TaxiMeterDatabase {
        return Room.databaseBuilder(
            context, TaxiMeterDatabase::class.java, "taximeter-db"
        ).build()
    }

    @Provides
    fun provideCostInfoDao(database: TaxiMeterDatabase): CostInfoDao {
        return database.costInfoDao()
    }
}