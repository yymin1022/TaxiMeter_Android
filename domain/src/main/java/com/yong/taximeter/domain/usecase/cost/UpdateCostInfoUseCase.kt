package com.yong.taximeter.domain.usecase.cost

import javax.inject.Inject

/**
 * Update Cost Info UseCase
 * - Check for update of cost info, and update it if needed
 * - Returns [UpdateCostInfoResult] as result
 */
class UpdateCostInfoUseCase @Inject constructor(
    // Inject dependency
) {
    suspend operator fun invoke(): UpdateCostInfoResult {
        // TODO: Implement update cost logic
        return UpdateCostInfoResult.SUCCESS
    }
}