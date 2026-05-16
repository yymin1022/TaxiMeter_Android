package com.yong.taximeter.data.repository

import com.yong.taximeter.domain.model.LocationData
import com.yong.taximeter.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    // Inject dependencies
): LocationRepository {
    override fun observeUpdate(): Flow<LocationData> {
        TODO("Implement")
    }
}