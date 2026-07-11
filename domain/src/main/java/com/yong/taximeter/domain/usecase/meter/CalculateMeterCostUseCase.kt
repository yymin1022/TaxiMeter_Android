package com.yong.taximeter.domain.usecase.meter

import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.model.MeterState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

/**
 * Calculate Meter Cost UseCase
 * - Emit [MeterState] on each location update
 * - Caller must ensure location permission is granted before collecting
 */
class CalculateMeterCostUseCase @Inject constructor(
    // Inject ObserveSpeed UseCase
    private val observeSpeedUseCase: ObserveSpeedUseCase,
) {
    /**
     * Start meter and emit [MeterState] on each location update
     *
     * @param costInfo Cost info for current region
     * @param isCityRate Whether to apply city surcharge
     */
    /**
     * Update events for meter calculation
     */
    private sealed interface MeterUpdateEvent {
        data class Speed(val speedData: SpeedData) : MeterUpdateEvent
        data class Surcharge(val isCityRate: Boolean) : MeterUpdateEvent
    }

    /**
     * Start meter and emit [MeterState] on each location update
     *
     * @param costInfo Cost info for current region
     * @param isCityRate Whether to apply city surcharge
     */
    operator fun invoke(
        costInfo: CostInfo,
        isCityRate: Flow<Boolean>,
    ): Flow<MeterState> {
        val speedEvents = observeSpeedUseCase().map { MeterUpdateEvent.Speed(it) }
        val surchargeEvents = isCityRate.map { MeterUpdateEvent.Surcharge(it) }

        return kotlinx.coroutines.flow.merge(speedEvents, surchargeEvents)
            .scan(MeterCostCalculator.newWithCostInfo(costInfo)) { acc, event ->
                when(event) {
                    is MeterUpdateEvent.Speed -> acc.update(event.speedData, acc.isCityRate)
                    is MeterUpdateEvent.Surcharge -> acc.updateSurcharge(event.isCityRate)
                }
            }
            .map { it.toMeterState() }
    }
}