package com.malikstudios.spacekayakapp.domain.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.malikstudios.spacekayakapp.data.local.LocalDataSource
import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.model.ServerState
import com.malikstudios.spacekayakapp.utils.NotificationHelper
import com.malikstudios.spacekayakapp.utils.toDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

// data/repository/ServerRepositoryImpl.kt

@Singleton
class ServerRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val appContext: Context // <-- Injected context for notifications
) : ServerRepository {

    private val mutex = Mutex()

    override fun getAllServers(): Flow<List<Server>> = localDataSource.getServersFlow()

    override suspend fun getServer(id: String): Server? = localDataSource.getServerById(id)

    override suspend fun provisionServer(server: Server) {
        val withDefaults = server.copy(
            updatedAt = System.currentTimeMillis(),
            state = ServerState.PENDING,
            ip = generateFakeIp()
        )
        localDataSource.insertServer(withDefaults)
    }

    override suspend fun updateServer(server: Server) {
        localDataSource.updateServer(
            server.copy(updatedAt = System.currentTimeMillis())
        )
    }

//    override suspend fun transitionState(id: String, newState: ServerState): Boolean = mutex.withLock {
//        return@withLock localDataSource.transitionState(id, newState)
//    }

    override suspend fun syncServersToCloud() = mutex.withLock {
        val servers = localDataSource.getServersFlow().first()

        servers.forEach { server ->
            val dto = server.toDto()
            firestore.collection("servers")
                .document(server.id)
                .set(dto)
        }
    }

    private fun generateFakeIp(): String {
        val random = (1..255).shuffled().take(4).joinToString(".")
        return random
    }

    override suspend fun transitionState(id: String, newState: ServerState): Boolean = mutex.withLock {
        val result = localDataSource.transitionState(id, newState)
        if (result) {
            val server = localDataSource.getServerById(id)
            server?.let {
                NotificationHelper.sendStateChangeNotification(
                    context = appContext, // Inject this via constructor
                    title = "Server ${newState.name}",
                    message = "Server '${it.name ?: "Unnamed"}' changed to ${newState.name}"
                )
            }
        }
        return@withLock result
    }

}
