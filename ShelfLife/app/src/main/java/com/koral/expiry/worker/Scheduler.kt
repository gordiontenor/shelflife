package com.koral.expiry.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object Scheduler {
    fun scheduleDaily(ctx: Context) {
        val req = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(24, TimeUnit.HOURS)
            .addTag("expiry-daily")
            .build()
        WorkManager.getInstance(ctx).enqueueUniquePeriodicWork(
            "expiry-daily", ExistingPeriodicWorkPolicy.UPDATE, req
        )
    }
}
