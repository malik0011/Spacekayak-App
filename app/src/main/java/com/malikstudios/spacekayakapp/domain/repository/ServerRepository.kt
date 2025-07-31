package com.malikstudios.spacekayakapp.domain.repository

import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.model.ServerState
import kotlinx.coroutines.flow.Flow

// domain/repository/ServerRepository.kt

interface ServerRepository {
    fun getAllServers(): Flow<List<Server>>
    suspend fun getServer(id: String): Server?
    suspend fun provisionServer(server: Server)
    suspend fun updateServer(server: Server)
    suspend fun transitionState(id: String, newState: ServerState): Boolean
    suspend fun syncServersToCloud()
}
