package com.koral.expiry.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class FoodItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val brand: String? = null,
    val barcode: String? = null,
    val expiryDate: LocalDate?,
    val photoPath: String? = null,
    val createdAtEpochSec: Long = System.currentTimeMillis() / 1000
)
