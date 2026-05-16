package com.yong.taximeter.domain.usecase.meter

import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.model.MeterState
import kotlinx.coroutines.flow.Flow
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
    operator fun invoke(costInfo: CostInfo, isCityRate: Boolean): Flow<MeterState> {
        return observeSpeedUseCase()
            .scan(MeterCostCalculator.newWithCostInfo(costInfo)) { acc, speedData ->
                acc.update(speedData, isCityRate)
            }
            .map { it.toMeterState() }
    }
}