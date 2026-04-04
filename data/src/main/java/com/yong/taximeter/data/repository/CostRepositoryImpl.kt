package com.yong.taximeter.data.repository

import com.yong.taximeter.data.datasource.FirestoreDataSource
import com.yong.taximeter.data.datasource.PreferenceDataSource
import com.yong.taximeter.data.dto.CostVersionDto
import com.yong.taximeter.domain.model.CostInfo
import com.yong.taximeter.domain.repository.CostRepository
import javax.inject.Inject
import kotlin.jvm.java

class CostRepositoryImpl @Inject constructor(
    // Inject Firestore DataSource
    private val firestoreDataSource: FirestoreDataSource,
    // Inject Preference DataSource
    private val preferenceDataSource: PreferenceDataSource,
): CostRepository {
    companion object {
        private const val FIRESTORE_COLLECTION_KEY_COST = "cost"
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