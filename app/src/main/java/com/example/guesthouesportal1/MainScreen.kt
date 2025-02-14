package com.example.guesthouesportal1

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    // State holders for user data
    var firstName by remember { mutableStateOf("Guest") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("guest@example.com") }
    var isDataLoading by remember { mutableStateOf(true) }

    // Retrieve user data from Firebase
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            try {
                val snapshot = FirebaseDatabase.getInstance().getReference("Users")
                    .child(currentUser.uid)
                    .get()
                    .await()
                val userData = snapshot.getValue(User::class.java)
                userData?.let {
                    firstName = it.firstName
                    lastName = it.lastName
                    email = it.email
                }
            } catch (e: Exception) {
                // Optionally log or handle the error here
            } finally {
                isDataLoading = false
            }
        } else {
            isDataLoading = false
        }
    }

    // Create a drawer state and coroutine scope for smooth open/close animations
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.animateContentSize() // Smooth animations when content changes
            ) {
                // Drawer Header: Branding / User Profile Information
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
//                        Icon(
//                            imageVector = Icons.Filled.Home,
//                            contentDescription = "User Profile",
//                            modifier = Modifier
//                                .size(64.dp)
//                                .clip(CircleShape)
//                        )
//                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "$firstName $lastName",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = email,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Drawer Items
                DrawerItem(
                    label = "Home",
                    icon = Icons.Filled.Home,
                    onClick = {
                        navController.navigate("main_screen")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    label = "Settings",
                    icon = Icons.Filled.Settings,
                    onClick = {
                        navController.navigate("settings")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    label = "Booking History",
                    icon = Icons.Filled.History,
                    onClick = {
                        navController.navigate("booking_history")
                        scope.launch { drawerState.close() }
                    }
                )
                DrawerItem(
                    label = "Help & Support",
                    icon = Icons.Filled.Help,
                    onClick = {
                        navController.navigate("help_support")
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Guest House Portal") },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Open Navigation Drawer"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* TODO: Implement a quick action */ },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add New")
                }
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text("Home") },
                        selected = false,
                        onClick = { navController.navigate("main_screen") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Build, contentDescription = "Reservation") },
                        label = { Text("Reservation") },
                        selected = false,
                        onClick = { navController.navigate("reservation") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Restaurant, contentDescription = "Dining") },
                        label = { Text("Dining") },
                        selected = false,
                        onClick = { navController.navigate("dining") }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = "Order Status") },
                        label = { Text("Order Status") },
                        selected = false,
                        onClick = { navController.navigate("order_status") }
                    )
                }
            }
        ) { innerPadding ->
            // Main content area with centered welcome text
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Welcome to the Guest House Portal!",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun DrawerItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(label) },
        icon = { Icon(imageVector = icon, contentDescription = label) },
        selected = false,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}
