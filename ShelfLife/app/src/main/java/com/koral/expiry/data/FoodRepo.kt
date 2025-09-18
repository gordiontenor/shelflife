package com.koral.expiry.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class FoodRepo(private val dao: FoodDao) {
    fun observeAll(): Flow<List<FoodItem>> = dao.observeAll()
    suspend fun add(item: FoodItem) = dao.insert(item)
    suspend fun update(item: FoodItem) = dao.update(item)
    suspend fun delete(item: FoodItem) = dao.delete(item)
    suspend fun dueUntil(until: LocalDate) = dao.dueUntil(until)
}
