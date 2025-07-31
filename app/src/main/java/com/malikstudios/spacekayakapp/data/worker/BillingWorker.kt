package com.malikstudios.spacekayakapp.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.malikstudios.spacekayakapp.domain.model.ServerState
import com.malikstudios.spacekayakapp.domain.repository.ServerRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

/*
 * BillingWorker.kt
 * This worker is responsible for billing servers based on their uptime.
 * It calculates the billing amount based on the time each server has been running.
 */

@HiltWorker
class BillingWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ServerRepository
) : CoroutineWorker(appContext, workerParams) {

    private val ratePerSecond = 0.1 // You can adjust this rate

    override suspend fun doWork(): Result {
        return try {
            val currentTime = System.currentTimeMillis()

            // Get all servers
            val servers = repository.getAllServers().first()

            // Calculate billing for each running server
            servers.filter { it.state == ServerState.RUNNING }.forEach { server ->
                val duration = (currentTime - server.lastBillingTimestamp) / 1000
                val additionalBilling = duration * ratePerSecond

                // Update server billing and timestamp
                val updated = server.copy(
                    uptime = server.uptime + duration,
                    lastBillingTimestamp = currentTime,
                    updatedAt = currentTime
                )

                // If the server has been running for more than 30 seconds, update its billing
                repository.updateServer(updated)
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("BillingWorker", "Billing failed: ${e.message}")
            Result.retry()
        }
    }
}
