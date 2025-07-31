package com.malikstudios.spacekayakapp.domain.model

import kotlinx.serialization.Serializable

/*
    * Space Kayak App
    * Created by Ayan on 10/20/2023.
 */
@Serializable
enum class ServerState {
    PENDING, RUNNING, STOPPED, TERMINATED
}