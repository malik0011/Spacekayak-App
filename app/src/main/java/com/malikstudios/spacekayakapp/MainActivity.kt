package com.malikstudios.spacekayakapp

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.malikstudios.spacekayakapp.presentation.navigation.AppNavGraph
import com.malikstudios.spacekayakapp.ui.theme.SpacekayakAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val isInitialized = FirebaseApp.getApps(this).isNotEmpty()
            Log.d("FirebaseCheck", "Is Firebase initialized? $isInitialized")


            // 🔐 1. Registering notification permission launcher-
            val requestPermissionLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    Log.d("MainActivity", "Notification permission granted")
                } else {
                    Log.d("MainActivity", "Notification permission denied")
                }
            }

            // Triggering permission check in LaunchedEffect-->
            LaunchedEffect(Unit) {
                //version check for POST_NOTIFICATIONS permission
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    //checking if the permission is not granted then requesting it
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            android.Manifest.permission.POST_NOTIFICATIONS
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // Launching the permission request
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                    }
                }
            }

            SpacekayakAppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text("Server Manager") })
                    }
                ) { innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding)){
                        AppNavGraph(navController)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpacekayakAppTheme {
//        Greeting("Android")
    }
}
