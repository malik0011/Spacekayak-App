package com.malikstudios.spacekayakapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.model.ServerState
import com.malikstudios.spacekayakapp.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ServerListViewModel @Inject constructor(
    private val getServersUseCase: GetServersUseCase,
    private val syncServersUseCase: SyncServersUseCase,
    private val transitionUseCase: TransitionServerStateUseCase
) : ViewModel() {

    private val _servers = MutableStateFlow<List<Server>>(emptyList())
    val servers: StateFlow<List<Server>> = _servers.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent

    init {
        observeServers()
    }

    private fun observeServers() {
        viewModelScope.launch {
            getServersUseCase().collect { list ->
                _servers.value = list
            }
        }
    }

    fun onSyncClicked() {
        viewModelScope.launch {
            try {
                syncServersUseCase()
                _toastEvent.emit("Synced successfully")
            } catch (e: Exception) {
                _toastEvent.emit("Sync failed")
            }
        }
    }

    fun onFsmAction(serverId: String, newState: ServerState) {
        viewModelScope.launch {
            val success = transitionUseCase(serverId, newState)
            if (!success) _toastEvent.emit("Invalid state transition")
        }
    }

    fun getBillingAmaount(): Double {
        var billingAmaount = 0.0
        _servers.value.forEach {
            if (it.state == ServerState.RUNNING) {
                billingAmaount += it.uptime * 0.1
            }
       }
        Log.d("testAyan", "getBillingAmaount: $billingAmaount")
        // Assuming 0.1 is the cost per hour for running servers
        return billingAmaount
    }
}
