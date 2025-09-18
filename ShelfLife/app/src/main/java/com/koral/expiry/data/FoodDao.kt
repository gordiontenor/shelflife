package com.koral.expiry.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface FoodDao {
    @Query("SELECT * FROM FoodItem ORDER BY expiryDate IS NULL, expiryDate ASC")
    fun observeAll(): Flow<List<FoodItem>>

    @Query("SELECT * FROM FoodItem WHERE expiryDate IS NOT NULL AND expiryDate <= :until ORDER BY expiryDate ASC")
    suspend fun dueUntil(until: LocalDate): List<FoodItem>

    @Insert suspend fun insert(item: FoodItem): Long
    @Update suspend fun update(item: FoodItem)
    @Delete suspend fun delete(item: FoodItem)
}
