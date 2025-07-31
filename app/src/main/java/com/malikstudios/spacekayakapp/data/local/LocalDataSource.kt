package com.malikstudios.spacekayakapp.data.local

import com.malikstudios.spacekayakapp.data.local.dao.ServerDao
import com.malikstudios.spacekayakapp.data.local.entities.ServerEntity
import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.model.ServerState
import com.malikstudios.spacekayakapp.utils.toDomain
import com.malikstudios.spacekayakapp.utils.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// data/local/LocalDataSource.kt

class LocalDataSource @Inject constructor(
    private val serverDao: ServerDao
) {

    fun getServersFlow(): Flow<List<Server>> =
        serverDao.getAllServersFlow().map { it.map(ServerEntity::toDomain) }

    suspend fun getServerById(id: String): Server? =
        serverDao.getServerById(id)?.toDomain()

    suspend fun insertServer(server: Server) =
        serverDao.insertServer(server.toEntity())

    suspend fun insertServers(servers: List<Server>) =
        serverDao.insertServers(servers.map { it.toEntity() })

    suspend fun updateServer(server: Server) =
        serverDao.updateServer(server.toEntity())

    suspend fun transitionState(id: String, newState: ServerState): Boolean =
        serverDao.transitionServerState(id, newState)

    suspend fun deleteServer(server: Server) =
        serverDao.deleteServer(server.toEntity())
}
