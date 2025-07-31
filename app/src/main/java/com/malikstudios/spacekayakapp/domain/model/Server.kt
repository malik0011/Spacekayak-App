package com.malikstudios.spacekayakapp.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Server(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val region: String = "",
    val ip: String = "",
    val state: ServerState = ServerState.PENDING,
    val uptime: Long = 0L,
    val lastBillingTimestamp: Long = 0L,
    val updatedAt: Long = 0L,
)

