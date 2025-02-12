package com.example.guesthouesportal1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import com.example.guesthouesportal1.ui.theme.GuestHousePortalTheme

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuestHousePortalTheme {
                MainScreen(authViewModel)
            }
        }
    }
}

@Composable
fun MainScreen(authViewModel: AuthViewModel) {
    AppNavigation(authViewModel)
}
