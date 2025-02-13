package com.example.guesthouesportal1

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                navigateToSignUp = { navController.navigate("signup") },
                navigateToForgotPassword = { navController.navigate("forgot_password") },
                navController
            )
        }
        composable("signup") {
            SignUpScreen(navigateToLogin = { navController.navigate("login") })
        }
        composable("forgot_password") {
            ForgotPasswordScreen(navigateToLogin = { navController.navigate("login") })
        }
        composable("main_screen") {
            MainScreen(navController) // Added main screen after login
        }
    }
}
