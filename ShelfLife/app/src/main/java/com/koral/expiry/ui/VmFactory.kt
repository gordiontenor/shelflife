package com.koral.expiry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.koral.expiry.data.FoodRepo
import com.koral.expiry.di.Di

class VmFactory(private val repo: FoodRepo): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repo) as T
    }
    companion object {
        fun default() = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repo = FoodRepo(Di.db.foodDao())
                return MainViewModel(repo) as T
            }
        }
    }
}
