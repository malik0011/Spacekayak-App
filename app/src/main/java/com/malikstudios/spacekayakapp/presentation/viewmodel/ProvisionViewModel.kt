package com.malikstudios.spacekayakapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikstudios.spacekayakapp.domain.usecase.ProvisionServerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProvisionViewModel @Inject constructor(
    private val provisionServerUseCase: ProvisionServerUseCase
) : ViewModel() {

    var serverName by mutableStateOf("")
    var serverType by mutableStateOf("Standard")
    var serverRegion by mutableStateOf("US")

    private val _provisionSuccess = MutableSharedFlow<Boolean>()
    val provisionSuccess: SharedFlow<Boolean> = _provisionSuccess

    fun onProvisionClick() {
        viewModelScope.launch {
            try {
                provisionServerUseCase(serverName, serverType, serverRegion)
                _provisionSuccess.emit(true)
            } catch (e: Exception) {
                _provisionSuccess.emit(false)
            }
        }
    }
}
