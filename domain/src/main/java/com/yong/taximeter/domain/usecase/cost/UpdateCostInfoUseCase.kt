package com.yong.taximeter.domain.usecase.cost

import com.yong.taximeter.core.common.AppLogger
import com.yong.taximeter.domain.repository.CostRepository
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

/**
 * Update Cost Info UseCase
 * - Check for update of cost info, and update it if needed
 * - Returns [UpdateCostInfoResult] as result
 */
class UpdateCostInfoUseCase @Inject constructor(
    // Inject App Logger
    private val appLogger: AppLogger,
    // Inject Cost Repository
    private val costRepository: CostRepository
) {
    suspend operator fun invoke(): UpdateCostInfoResult {
        return try {
            // Get local / remote version info
            val localVersion = costRepository.getLocalVersion()
            val remoteVersion = costRepository.getRemoteVersion()

            // Check if update needed
            if(localVersion >= remoteVersion) {
                // If not needed, return up-to-date
                return UpdateCostInfoResult.UP_TO_DATE
            }

            // Update to latest cost info
            val updateResult = costRepository.updateToLatest()

            // Return update result
            if(updateResult) {
                UpdateCostInfoResult.SUCCESS
            } else {
                UpdateCostInfoResult.FAILURE
            }
        } catch(_: CancellationException) {
            // If canceled, just return without exception
            UpdateCostInfoResult.CANCELED
        } catch(e: Exception) {
            // Log exception
            appLogger.recordException(e)
            UpdateCostInfoResult.FAILURE
        }
    }
}