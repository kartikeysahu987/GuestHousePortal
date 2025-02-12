package com.example.guesthouesportal1

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignUpScreen(authViewModel: AuthViewModel = viewModel(), navigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                Toast.makeText(context, (authState as AuthState.Success).message, Toast.LENGTH_SHORT).show()
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).error, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())

        Button(onClick = { authViewModel.signUp(email, password) }) { Text("Sign Up") }
        TextButton(onClick = navigateToLogin) { Text("Already have an account? Login") }
    }
}
