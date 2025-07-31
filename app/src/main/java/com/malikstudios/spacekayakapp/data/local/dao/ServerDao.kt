package com.malikstudios.spacekayakapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.malikstudios.spacekayakapp.data.local.entities.ServerEntity
import com.malikstudios.spacekayakapp.domain.model.ServerState
import kotlinx.coroutines.flow.Flow

// data/local/dao/ServerDao.kt

@Dao
interface ServerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServer(server: ServerEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServers(servers: List<ServerEntity>)

    @Query("SELECT * FROM servers ORDER BY updatedAt DESC")
    fun getAllServersFlow(): Flow<List<ServerEntity>>

    @Query("SELECT * FROM servers WHERE id = :id")
    suspend fun getServerById(id: String): ServerEntity?

    @Update
    suspend fun updateServer(server: ServerEntity)

    @Delete
    suspend fun deleteServer(server: ServerEntity)

    @Transaction
    suspend fun transitionServerState(id: String, newState: ServerState): Boolean {
        val server = getServerById(id) ?: return false
        // Validate transition
        if (isValidTransition(server.state, newState)) {
            val updated = server.copy(
                state = newState,
                updatedAt = System.currentTimeMillis()
            )
            updateServer(updated)
            return true
        }
        return false
    }

    // Private FSM transition check
    private fun isValidTransition(current: ServerState, next: ServerState): Boolean {
        return when (current) {
            ServerState.PENDING -> next == ServerState.RUNNING
            ServerState.RUNNING -> next == ServerState.STOPPED || next == ServerState.TERMINATED
            ServerState.STOPPED -> next == ServerState.RUNNING || next == ServerState.TERMINATED
            ServerState.TERMINATED -> false
        }
    }
}
