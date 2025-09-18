package com.koral.expiry.di

import android.content.Context
import androidx.room.Room
import com.koral.expiry.data.AppDatabase

object Di {
    lateinit var db: AppDatabase
        private set

    fun init(context: Context) {
        db = Room.databaseBuilder(context, AppDatabase::class.java, "expiry.db").build()
    }
}
