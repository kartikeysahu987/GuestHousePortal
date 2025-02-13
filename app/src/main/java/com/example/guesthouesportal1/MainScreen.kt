package com.example.guesthouesportal1

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    // Create a drawer state and coroutine scope for controlling drawer actions
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.padding(16.dp)) {
                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = false,
                        onClick = {
                            // TODO: Navigate to Home and close the drawer
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        onClick = {
                            // TODO: Navigate to Settings and close the drawer
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Booking History") },
                        selected = false,
                        onClick = {
                            // TODO: Navigate to Booking History and close the drawer
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Help & Support") },
                        selected = false,
                        onClick = {
                            // TODO: Navigate to Help & Support and close the drawer
                            scope.launch { drawerState.close() }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Guest House Portal") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Build, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = false,
                        onClick = { /* TODO: Navigate to Home */ }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Build, contentDescription = "Reservations") },
                        label = { Text("Reservations") },
                        selected = false,
                        onClick = { /* TODO: Navigate to Reservations */ }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Build, contentDescription = "Dining") },
                        label = { Text("Dining") },
                        selected = false,
                        onClick = { /* TODO: Navigate to Dining */ }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Order Status") },
                        label = { Text("Order Status") },
                        selected = false,
                        onClick = { /* TODO: Navigate to Order Status */ }
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to the Guest House Portal!",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}
