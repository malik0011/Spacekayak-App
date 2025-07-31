package com.malikstudios.spacekayakapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.malikstudios.spacekayakapp.domain.model.Server
import com.malikstudios.spacekayakapp.domain.model.ServerState


@Composable
fun ServerListScreen(
    servers: List<Server>,
    onSync: () -> Unit,
    onFsmAction: (String, ServerState) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onSync, modifier = Modifier.fillMaxWidth()) {
            Text("Sync to Cloud")
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(servers) { server ->
                ServerItem(server, onFsmAction)
                Divider()
            }
        }
    }
}

@Composable
fun ServerItem(server: Server, onFsmAction: (String, ServerState) -> Unit) {
    Column(Modifier.padding(8.dp)) {
        Text("Name: ${server.name ?: "Unnamed"}")
        Text("State: ${server.state}")
        Text("Region: ${server.region}")
        Text("IP: ${server.ip}")

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            when (server.state) {
                ServerState.PENDING -> {
                    Button(onClick = { onFsmAction(server.id, ServerState.RUNNING) }) {
                        Text("Start")
                    }
                }
                ServerState.RUNNING -> {
                    Button(onClick = { onFsmAction(server.id, ServerState.STOPPED) }) {
                        Text("Stop")
                    }
                    Button(onClick = { onFsmAction(server.id, ServerState.TERMINATED) }) {
                        Text("Terminate")
                    }
                }
                ServerState.STOPPED -> {
                    Button(onClick = { onFsmAction(server.id, ServerState.RUNNING) }) {
                        Text("Restart")
                    }
                    Button(onClick = { onFsmAction(server.id, ServerState.TERMINATED) }) {
                        Text("Terminate")
                    }
                }
                ServerState.TERMINATED -> {
                    Text("No actions available", color = Color.Gray)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ServerListPreview() {
    ServerListScreen(
        servers = listOf(
            Server("1", "Test Server", "Standard", "US", "192.168.0.1", ServerState.PENDING, 0, 0, 0),
            Server("2", "Running Server", "Large", "IN", "10.0.0.2", ServerState.RUNNING, 100, 0, 0)
        ),
        onSync = {},
        onFsmAction = { _, _ -> }
    )
}

