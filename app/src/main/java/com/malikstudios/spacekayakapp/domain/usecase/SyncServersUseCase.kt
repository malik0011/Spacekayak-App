package com.malikstudios.spacekayakapp.domain.usecase

import com.malikstudios.spacekayakapp.domain.repository.ServerRepository
import javax.inject.Inject

// domain/usecase/SyncServersUseCase.kt

/**
 * Use case to manually sync local servers to Firestore.
 */
class SyncServersUseCase @Inject constructor(
    private val repository: ServerRepository
) {
    suspend operator fun invoke() {
        repository.syncServersToCloud()
    }
}
