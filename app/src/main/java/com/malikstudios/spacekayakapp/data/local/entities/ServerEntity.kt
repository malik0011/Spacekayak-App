package com.malikstudios.spacekayakapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malikstudios.spacekayakapp.domain.model.ServerState

@Entity(tableName = "servers")
data class ServerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val type: String,
    val region: String,
    val ip: String,
    val state: ServerState,
    val uptime: Long,
    val lastBillingTimestamp: Long,
    val updatedAt: Long,
//    val startTime: Long? = null,                  // when STARTED
//    val totalActiveTimeSeconds: Long = 0L
)
