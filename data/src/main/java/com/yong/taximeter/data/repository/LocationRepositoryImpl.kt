package com.yong.taximeter.data.repository

import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.yong.taximeter.domain.model.LocationData
import com.yong.taximeter.domain.repository.LocationRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    // Inject GMS Location Provider Client
    private val fusedLocationClient: FusedLocationProviderClient,
    // Inject GMS Location Request Instance
    private val locationRequest: LocationRequest,
): LocationRepository {
    /**
     * Observe location updates from [FusedLocationProviderClient]
     * - Emits [LocationData] on each location update
     */
    override fun observeUpdate(): Flow<LocationData> = callbackFlow {
        // Generate callback for GMS Location Provider
        val callback = object: LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(
                        LocationData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            accuracyMeters = location.accuracy,
                            timestampMillis = location.time,
                        )
                    )
                }
            }
        }

        // Register callback
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper(),
        )

        // Remove callback when close
        awaitClose { fusedLocationClient.removeLocationUpdates(callback) }
    }
}