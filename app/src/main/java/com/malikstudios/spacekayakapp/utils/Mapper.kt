package com.malikstudios.spacekayakapp.utils

import com.malikstudios.spacekayakapp.data.local.entities.ServerEntity
import com.malikstudios.spacekayakapp.data.remote.dto.ServerDto
import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.model.ServerState

// utils/Mapper.kt

fun ServerEntity.toDomain() = Server(
    id, name, type, region, ip, state, uptime, lastBillingTimestamp, updatedAt
)

fun Server.toEntity() = ServerEntity(
    id, name, type, region, ip, state, uptime, lastBillingTimestamp, updatedAt
)

fun ServerDto.toDomain(): Server = Server(
    id, name, type, region, ip,
    ServerState.valueOf(state),
    uptime, lastBillingTimestamp, updatedAt
)

fun Server.toDto(): ServerDto = ServerDto(
    id, name, type, region, ip, state.name, uptime, lastBillingTimestamp, updatedAt
)
