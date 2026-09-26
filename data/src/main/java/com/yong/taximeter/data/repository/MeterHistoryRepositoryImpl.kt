package com.yong.taximeter.data.repository

import com.yong.taximeter.data.dao.MeterHistoryDao
import com.yong.taximeter.data.mapper.MeterHistoryMapper.toDomain
import com.yong.taximeter.data.mapper.MeterHistoryMapper.toEntity
import com.yong.taximeter.domain.model.MeterHistory
import com.yong.taximeter.domain.repository.MeterHistoryRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Implementation of [MeterHistoryRepository]
 */
class MeterHistoryRepositoryImpl @Inject constructor(
    private val dao: MeterHistoryDao,
): MeterHistoryRepository {
    override suspend fun insertHistory(history: MeterHistory) {
        dao.insertHistory(history.toEntity())
    }

    override fun getAllHistories(): Flow<List<MeterHistory>> {
        return dao.getAllHistories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteHistory(id: Long) {
        dao.deleteHistoryById(id)
    }

    override suspend fun deleteAllHistories() {
        dao.deleteAllHistories()
    }
}
