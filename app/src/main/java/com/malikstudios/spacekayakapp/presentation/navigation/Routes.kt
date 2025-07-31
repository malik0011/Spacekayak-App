package com.malikstudios.spacekayakapp.presentation.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Provision : Screen("provision")
    data object ServerList : Screen("server_list")
}