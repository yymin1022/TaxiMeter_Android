package com.yong.taximeter.domain.usecase.meter

import com.yong.taximeter.domain.defs.MeterDefs
import com.yong.taximeter.domain.model.LocationData
import com.yong.taximeter.domain.model.MeterStatus
import com.yong.taximeter.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import javax.inject.Inject
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Observe Speed UseCase
 * - Observe speed data from location updates
 * - Calculate speed and distance using Haversine formula
 */
class ObserveSpeedUseCase @Inject constructor(
    // Inject Location Repository
    private val locationRepository: LocationRepository,
) {
    /**
     * Observe speed data from location updates
     * - Return [MeterStatus.GPS_ERROR] if accuracy is below threshold
     * - Caller must ensure location permission is granted before collecting
     */
    operator fun invoke(): Flow<SpeedData> =
        locationRepository.observeUpdate()
            .scan<LocationData, Pair<LocationData?, SpeedData>>(null to SpeedData.ZERO) { (prev, _), current ->
                val speedData = calculateSpeedData(prev, current)
                current to speedData
            }
            .map { (_, speedData) -> speedData }

    /**
     * Calculate [SpeedData] from previous and current [LocationData]
     * - Return GPS error state if accuracy is below threshold
     * - Return zero speed if no previous location
     */
    private fun calculateSpeedData(prev: LocationData?, current: LocationData): SpeedData {
        // Check if accuracy is low
        if(current.accuracyMeters > MeterDefs.GPS_ACCURACY_THRESHOLD) {
            return SpeedData(
                distanceDeltaMeters = 0.0,
                elapsedDeltaSeconds = if (prev != null)
                    (current.timestampMillis - prev.timestampMillis) / 1000.0 else 0.0,
                speedKph = 0.0,
                status = MeterStatus.GPS_ERROR,
            )
        }

        // If prev location is null, set as Zero
        if(prev == null) return SpeedData.ZERO

        val elapsedDeltaSeconds = (current.timestampMillis - prev.timestampMillis) / 1000.0
        if(elapsedDeltaSeconds <= 0) return SpeedData.ZERO

        // Calculate distance using Haversine formula
        val distanceDeltaMeters = haversineDistanceMeters(prev, current)
        val speedKph = (distanceDeltaMeters / elapsedDeltaSeconds) * MPS_TO_KPH

        return SpeedData(
            distanceDeltaMeters = distanceDeltaMeters,
            elapsedDeltaSeconds = elapsedDeltaSeconds,
            speedKph = speedKph.coerceAtLeast(0.0),
            status = MeterStatus.RUNNING,
        )
    }

    /**
     * Calculate distance between two [LocationData] using Haversine formula
     *
     * @return distance in meters
     */
    private fun haversineDistanceMeters(from: LocationData, to: LocationData): Double {
        val lat1 = Math.toRadians(from.latitude)
        val lat2 = Math.toRadians(to.latitude)
        val dLat = Math.toRadians(to.latitude - from.latitude)
        val dLon = Math.toRadians(to.longitude - from.longitude)

        val a = sin(dLat / 2).pow(2) +
                cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)

        return EARTH_RADIUS_METERS * 2 * asin(sqrt(a))
    }

    companion object {
        private const val MPS_TO_KPH = 3.6
        private const val EARTH_RADIUS_METERS = 6_371_000.0
    }
}