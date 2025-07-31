package com.malikstudios.spacekayakapp.domain.usecase

import com.malikstudios.spacekayakapp.domain.model.ServerState
import com.malikstudios.spacekayakapp.domain.repository.ServerRepository
import javax.inject.Inject

/**
 * Use case to safely change the server's FSM state.
 */
class TransitionServerStateUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    suspend operator fun invoke(serverId: String, newState: ServerState): Boolean {
        return repository.transitionState(serverId, newState)
    }
}
