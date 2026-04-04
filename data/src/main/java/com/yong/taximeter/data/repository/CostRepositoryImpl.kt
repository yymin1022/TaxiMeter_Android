package com.yong.taximeter.data.repository

import com.yong.taximeter.data.dao.CostInfoDao
import com.yong.taximeter.data.datasource.FirestoreDataSource
import com.yong.taximeter.data.datasource.PreferenceDataSource
import com.yong.taximeter.data.dto.CostInfoDto
import com.yong.taximeter.data.dto.CostVersionDto
import com.yong.taximeter.data.mapper.CostInfoMapper.toDomain
import com.yong.taximeter.data.mapper.CostInfoMapper.toEntity
import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.repository.CostRepository
import javax.inject.Inject
import kotlin.jvm.java

class CostRepositoryImpl @Inject constructor(
    // Inject Cost DB DAO
    private val costDao: CostInfoDao,
    // Inject Firestore DataSource
    private val firestoreDataSource: FirestoreDataSource,
    // Inject Preference DataSource
    private val preferenceDataSource: PreferenceDataSource,
): CostRepository {
    companion object {
        private const val FIRESTORE_COLLECTION_KEY_COST = "cost"
        private const val FIRESTORE_DOCUMENT_KEY_INFO = "info"
        private const val FIRESTORE_DOCUMENT_KEY_VERSION = "version"

        private const val PREF_KEY_COST_VERSION = "PREF_KEY_COST_VERSION"

        private const val COST_VERSION_FALLBACK = "20001022"
    }

    /**
     * Get local cost version from Preference
     * - If null or error, return [COST_VERSION_FALLBACK]
     */
    override fun getLocalVersion(): String {
        return preferenceDataSource.getString(
            key = PREF_KEY_COST_VERSION,
            defaultValue = COST_VERSION_FALLBACK,
        )
    }

    /**
     * Get remote cost version from Firestore
     * - If null or error, return [COST_VERSION_FALLBACK]
     */
    override suspend fun getRemoteVersion(): String {
        return firestoreDataSource.getDocumentObject(
            collection = FIRESTORE_COLLECTION_KEY_COST,
            document = FIRESTORE_DOCUMENT_KEY_VERSION,
            clazz = CostVersionDto::class.java,
        )?.version ?: COST_VERSION_FALLBACK
    }

    /**
     * Get remote cost version from Firestore
     * - If null or error, return [COST_VERSION_FALLBACK]
     *
     * @return true if update is successful, false otherwise
     */
    override suspend fun updateToLatest(): Boolean {
        val costListDto = firestoreDataSource.getDocumentObject(
            collection = FIRESTORE_COLLECTION_KEY_COST,
            document = FIRESTORE_DOCUMENT_KEY_INFO,
            clazz = CostInfoDto::class.java,
        )

        // If cost info is unavailable, return false
        if(costListDto?.data.isNullOrEmpty()) return false

        // Convert to entity, and update DAO
        val costEntityList = costListDto.data.map { it.toEntity() }
        costDao.updateRemoteCost(costEntityList)

        // Get remote version info
        val remoteVersion = getRemoteVersion()

        // Update local version info
        preferenceDataSource.setString(PREF_KEY_COST_VERSION, remoteVersion)

        // Update succeed. return true
        return true
    }

    /**
     * Get cost info of [regionKey]
     *
     * @return [CostInfo] instance
     * - If null, there is no such region
     */
    override fun getCostInfo(
        regionKey: String
    ): CostInfo? {
        // Get entity from DAO, and convert to model
        return costDao.getByRegion(regionKey)
            ?.toDomain()
    }
}