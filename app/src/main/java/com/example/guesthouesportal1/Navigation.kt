package com.example.guesthouesportal1

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(authViewModel) { navController.navigate("signup") } }
        composable("signup") { SignUpScreen(authViewModel) { navController.navigate("login") } }
    }
}
