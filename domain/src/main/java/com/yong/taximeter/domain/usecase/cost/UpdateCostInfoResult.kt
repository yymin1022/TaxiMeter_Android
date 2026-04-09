package com.yong.taximeter.domain.usecase.cost

/**
 * Update Cost Info Result
 * - Result of [UpdateCostInfoUseCase] invoke
 */
enum class UpdateCostInfoResult {
    UP_TO_DATE,
    SUCCESS,
    FAILURE,
    CANCELED,
}