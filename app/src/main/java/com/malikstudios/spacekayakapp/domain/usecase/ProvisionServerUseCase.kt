package com.malikstudios.spacekayakapp.domain.usecase

import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.model.ServerState
import com.malikstudios.spacekayakapp.domain.repository.ServerRepository
import java.util.UUID
import javax.inject.Inject

/**
 * Use case to create a new server with default values.
 */
class ProvisionServerUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    suspend operator fun invoke(name: String?, type: String, region: String) {
        val server = Server(
            id = UUID.randomUUID().toString(),
            name = name?: "",
            type = type,
            region = region,
            ip = "", // will be assigned in repository
            state = ServerState.PENDING,
            uptime = 0,
            lastBillingTimestamp = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        repository.provisionServer(server)
    }
}
