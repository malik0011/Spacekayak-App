package com.malikstudios.spacekayakapp.data.remote.dto

// data/remote/dto/ServerDto.kt
data class ServerDto(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val region: String = "",
    val ip: String = "",
    val state: String = "PENDING",
    val uptime: Long = 0,
    val lastBillingTimestamp: Long = 0,
    val updatedAt: Long = 0
)
