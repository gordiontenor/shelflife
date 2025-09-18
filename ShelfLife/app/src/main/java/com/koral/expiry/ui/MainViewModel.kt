package com.koral.expiry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koral.expiry.data.FoodItem
import com.koral.expiry.data.FoodRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainViewModel(private val repo: FoodRepo): ViewModel() {
    val items = repo.observeAll().stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun add(name: String, expiry: LocalDate?, photoPath: String?, brand: String? = null) {
        viewModelScope.launch {
            repo.add(FoodItem(name = name, expiryDate = expiry, photoPath = photoPath, brand = brand))
        }
    }

    fun delete(item: com.koral.expiry.data.FoodItem) = viewModelScope.launch { repo.delete(item) }
}
