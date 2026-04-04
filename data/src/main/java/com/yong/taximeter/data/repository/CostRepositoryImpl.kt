package com.yong.taximeter.data.repository

import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.repository.CostRepository
import javax.inject.Inject

class CostRepositoryImpl @Inject constructor(
    // Inject constructor
): CostRepository {
    override fun getLocalVersion(): String {
        // TODO: Implement logic
        return "LOCAL_VERSION"
    }

    override fun getRemoteVersion(): String {
        // TODO: Implement logic
        return "REMOTE_VERSION"
    }

    override fun updateToLatest(): Boolean {
        // TODO: Implement logic
        return false
    }

    override fun getCostInfo(
        regionKey: String
    ): CostInfo? {
        // TODO: Implement logic
        return CostInfo()
    }
}