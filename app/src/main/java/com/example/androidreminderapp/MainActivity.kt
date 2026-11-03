package com.example.androidreminderapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.androidreminderapp.ui.auth.AuthenticatedLandingScreen
import com.example.androidreminderapp.ui.auth.LoginScreen
import com.example.androidreminderapp.ui.theme.AndroidReminderAppTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        // Install splash screen before super.onCreate()
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Determine the initial destination once, outside Compose.
        val startDestination = try {
            val firebaseAuth = FirebaseAuth.getInstance()

            if (firebaseAuth.currentUser != null) {
                "home"
            } else {
                "login"
            }

        } catch (exception: Exception) {
            Log.e(
                "MainActivity",
                "Unable to retrieve Firebase authentication session",
                exception
            )
            "login"
        }

        setContent {
            AndroidReminderAppTheme {

                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {

                        // LOGIN SCREEN
                        composable("login") {

                            LoginScreen(
                                onLoginSuccess = {

                                    navController.navigate("home") {

                                        popUpTo("login") {
                                            inclusive = true
                                        }

                                        launchSingleTop = true
                                    }
                                }
                            )
                        }

                        // HOME / DASHBOARD SCREEN
                        composable("home") {

                            AuthenticatedLandingScreen(
                                onLogout = {
                                    navController.navigate("login") {
                                        popUpTo("home") {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
