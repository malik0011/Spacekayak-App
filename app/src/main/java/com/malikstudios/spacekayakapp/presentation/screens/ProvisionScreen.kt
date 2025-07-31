package com.malikstudios.spacekayakapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ProvisionScreen(
    name: String,
    type: String,
    region: String,
    onNameChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onProvisionClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Server Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = type,
            onValueChange = onTypeChange,
            label = { Text("Server Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = region,
            onValueChange = onRegionChange,
            label = { Text("Region") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onProvisionClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Provision Server")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProvisionScreenPreview() {
    ProvisionScreen(
        name = "MyServer",
        type = "Standard",
        region = "US",
        onNameChange = {},
        onTypeChange = {},
        onRegionChange = {},
        onProvisionClick = {}
    )
}
