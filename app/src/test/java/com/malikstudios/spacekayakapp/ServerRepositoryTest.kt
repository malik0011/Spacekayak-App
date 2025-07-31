package com.malikstudios.spacekayakapp

import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.model.ServerState
import com.malikstudios.spacekayakapp.domain.repository.ServerRepository
import com.malikstudios.spacekayakapp.presentation.viewmodel.ProvisionViewModel
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ServerRepositoryTest {

    private lateinit var repository: ServerRepository
    private lateinit var viewModel: ProvisionViewModel

    @Before
    fun setup() {
        repository = mockk(relaxed = true) // Use relaxed to avoid null issues
        viewModel = ProvisionViewModel(
            provisionServerUseCase = mockk(relaxed = true) // Mock the use case
        )
    }

    @Test
    fun `startServer transitions state to RUNNING`() = runTest {
        // Given: a server in PENDING state
        val server = Server(id = "1", state = ServerState.PENDING)

        // When: transitionState is expected to return success
        coEvery { repository.transitionState(any(), any()) } returns true

        // Set up input form values
        viewModel.apply {
            serverName = server.name
            serverType = server.type
            serverRegion = server.region
        }

        // Trigger provisioning
        viewModel.onProvisionClick()

        // Then: verify repository call
        coVerify(exactly = 1) {
            repository.transitionState(any(), ServerState.RUNNING)
        }
    }

}