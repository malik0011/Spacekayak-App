package com.malikstudios.spacekayakapp.presentation.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.malikstudios.spacekayakapp.presentation.screens.DashboardScreen
import com.malikstudios.spacekayakapp.presentation.screens.ProvisionScreen
import com.malikstudios.spacekayakapp.presentation.screens.ServerListScreen
import com.malikstudios.spacekayakapp.presentation.viewmodel.ProvisionViewModel
import com.malikstudios.spacekayakapp.presentation.viewmodel.ServerListViewModel

/*
    * AppNavGraph.kt
    * This file defines the navigation graph for the Space Kayak app.
 */
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Dashboard.route) {

        composable(Screen.Dashboard.route) {
            val serversViewModel: ServerListViewModel = hiltViewModel()

            DashboardScreen(
                totalServers = serversViewModel.servers.collectAsState().value.size,
                totalBilling = 0.0, // Add billing logic later
                onProvisionClick = {
                    navController.navigate(Screen.Provision.route)
                },
                onServerListClick = {
                    navController.navigate(Screen.ServerList.route)
                }
            )
        }

        composable(Screen.Provision.route) {
            val provisionViewModel: ProvisionViewModel = hiltViewModel()
            ProvisionScreen(
                name = provisionViewModel.serverName,
                type = provisionViewModel.serverType,
                region = provisionViewModel.serverRegion,
                onNameChange = { provisionViewModel.serverName = it },
                onTypeChange = { provisionViewModel.serverType = it },
                onRegionChange = { provisionViewModel.serverRegion = it },
                onProvisionClick = {
                    provisionViewModel.onProvisionClick()
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.ServerList.route) {
            val serverListViewModel: ServerListViewModel = hiltViewModel()

            val servers by serverListViewModel.servers.collectAsState()
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                serverListViewModel.toastEvent.collect { msg ->
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }

            ServerListScreen(
                servers = servers,
                onSync = { serverListViewModel.onSyncClicked() },
                onFsmAction = { id, newState ->
                    serverListViewModel.onFsmAction(id, newState)
                }
            )
        }
    }
}
