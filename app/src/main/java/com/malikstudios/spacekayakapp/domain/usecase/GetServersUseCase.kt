package com.malikstudios.spacekayakapp.domain.usecase

import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.repository.ServerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Use case to get the flow of all servers.
 */
class GetServersUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    operator fun invoke(): Flow<List<Server>> = repository.getAllServers()
}
