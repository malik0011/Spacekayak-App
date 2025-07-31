package com.malikstudios.spacekayakapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// presentation/screens/DashboardScreen.kt

@Composable
fun DashboardScreen(
    totalServers: Int,
    totalBilling: Double,
    onProvisionClick: () -> Unit,
    onServerListClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Total Servers: $totalServers", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("Total Billing: ₹$totalBilling", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(24.dp))
        Button(onClick = onProvisionClick) {
            Text("Provision New Server")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onServerListClick) {
            Text("View Servers") // ✅ This button navigates to Server List
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    DashboardScreen(
        totalServers = 5,
        totalBilling = 125.0,
        onProvisionClick = {},
        onServerListClick = {}
    )
}
