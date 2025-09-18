package com.koral.expiry.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.koral.expiry.di.Di
import com.koral.expiry.notify.Notif
import java.time.LocalDate

class ExpiryCheckWorker(ctx: Context, params: WorkerParameters): CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        val dao = Di.db.foodDao()
        val today = LocalDate.now()
        val warnUntil = today.plusDays(3)
        val due = dao.dueUntil(warnUntil)

        if (due.isNotEmpty()) {
            val soon = due.filter { it.expiryDate != null && it.expiryDate!! >= today }
            val expired = due.filter { it.expiryDate != null && it.expiryDate!! < today }

            if (soon.isNotEmpty()) {
                Notif.show(applicationContext, "Yaklaşıyor",
                    soon.joinToString { "${it.name} → ${it.expiryDate}" })
            }
            if (expired.isNotEmpty()) {
                Notif.show(applicationContext, "Süren Doldu",
                    expired.joinToString { "${it.name} → ${it.expiryDate}" })
            }
        }
        return Result.success()
    }
}
