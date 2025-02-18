package com.example.guesthouesportal1

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navigateToSignUp = { navController.navigate("signup") },
                navigateToForgotPassword = { navController.navigate("forgot_password") },
                navController = navController
            )
        }
        composable("signup") {
            SignUpScreen(navigateToLogin = { navController.navigate("login") })
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navigateToLogin = { navController.navigate("login") })
        }
        composable("main_screen") {
            MainScreen(navController = navController) // Main Screen with drawer
        }
        composable("settings") {
            SettingsScreen(navController = navController) // Settings page for profile picture changes
        }
        composable("dining") {
            DiningScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}
