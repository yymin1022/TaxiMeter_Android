package com.yong.taximeter.domain.repository

import com.yong.taximeter.domain.model.LocationData
import kotlinx.coroutines.flow.Flow

/**
 * Location Repository Interface
 * - Observe real-time location updates
 * - Location update is represented with [LocationData] class.
 */
interface LocationRepository {
    // Observe location updates
    fun observeUpdate(): Flow<LocationData>
}