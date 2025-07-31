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
    * BootWorker.kt
    * This worker is responsible for bootstrapping the server states on app startup.
    * It transitions all servers in PENDING state to RUNNING state.
 */

@HiltWorker
class BootWorker @AssistedInject constructor(
    @Assisted appContext: Context, // Application context
    @Assisted workerParams: WorkerParameters, // Worker parameters
    private val repository: ServerRepository // Injected repository to access server data
) : CoroutineWorker(appContext, workerParams) { // CoroutineWorker allows us to perform background work using coroutines

    override suspend fun doWork(): Result {
        return try {
            // Fetch all servers
            val servers = repository.getAllServers().first()

            // Transition all servers in PENDING state to RUNNING state
            servers.filter { it.state == ServerState.PENDING }
                .forEach { server ->
                    // Atomically transition from PENDING -> RUNNING
                    repository.transitionState(server.id, ServerState.RUNNING)
                }

            Result.success()
        } catch (e: Exception) {
            Log.e("BootWorker", "Boot failed: ${e.message}")
            Result.retry() // Retry if failed
        }
    }
}
