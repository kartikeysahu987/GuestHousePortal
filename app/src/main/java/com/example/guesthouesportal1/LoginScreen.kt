package com.example.guesthouesportal1

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigateToSignUp: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navController: NavController
) {
    // Retain existing ViewModel integration
    val authViewModel: AuthViewModel = viewModel()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val authState by authViewModel.authState.collectAsState()

    // Focus requesters for keyboard handling
    val emailFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    // Handle authentication state changes
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Loading -> {
                // Loading state can be handled here if needed
            }
            is AuthState.Success -> {
                val message = (authState as AuthState.Success).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                navController.navigate("main_screen")
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).error, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    // Overall screen background using a modern gradient
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f), // Semi-transparent form container
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Guest House Portal", style = MaterialTheme.typography.titleLarge) },
                    modifier = Modifier.shadow(4.dp)
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .imePadding() // Adapts to the keyboard
                    .animateContentSize(), // Smooth motion for layout changes
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Vibrant, colorful "Login" header using gradient text with bold font weight
                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary,
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Email Input Field with validation and automatic lowercase conversion
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        // Convert email input to lowercase to handle Gmail (and similar) addresses
                        val lowerCaseEmail = it.lowercase()
                        email = lowerCaseEmail
                        emailError = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(lowerCaseEmail).matches() && lowerCaseEmail.isNotEmpty()) {
                            "Invalid email address"
                        } else null
                    },
                    label = { Text("Email") },
                    placeholder = { Text("example@mail.com") },
                    singleLine = true,
                    isError = emailError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(emailFocusRequester),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        autoCorrect = false,
                        capitalization = KeyboardCapitalization.None
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { passwordFocusRequester.requestFocus() }
                    )
                )
                AnimatedVisibility(visible = emailError != null, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        text = emailError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Password Input Field with text toggle for visibility and validation
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = if (it.length < 6 && it.isNotEmpty()) {
                            "Password too short"
                        } else null
                    },
                    label = { Text("Password") },
                    placeholder = { Text("Enter your password") },
                    singleLine = true,
                    trailingIcon = {
                        TextButton(
                            onClick = { isPasswordVisible = !isPasswordVisible },
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            Text(if (isPasswordVisible) "Hide" else "Show")
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    isError = passwordError != null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocusRequester),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    keyboardActions = KeyboardActions(
                        onDone = { keyboardController?.hide() }
                    )
                )
                AnimatedVisibility(visible = passwordError != null, enter = fadeIn(), exit = fadeOut()) {
                    Text(
                        text = passwordError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))

                // Login Button with smooth animations, haptic feedback and loading indicator
                Button(
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        if (email.isBlank()) {
                            emailError = "Email cannot be empty"
                            return@Button
                        }
                        if (password.isBlank()) {
                            passwordError = "Password cannot be empty"
                            return@Button
                        }
                        keyboardController?.hide()
                        authViewModel.login(email, password)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = authState !is AuthState.Loading
                ) {
                    if (authState is AuthState.Loading) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text("Login")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Navigation options with accessibility labels
                TextButton(onClick = navigateToSignUp) {
                    Text(
                        "Don't have an account? Sign Up",
                        modifier = Modifier.semantics { contentDescription = "Navigate to Sign Up" }
                    )
                }
                TextButton(onClick = navigateToForgotPassword) {
                    Text(
                        "Forgot Password?",
                        modifier = Modifier.semantics { contentDescription = "Navigate to Forgot Password" }
                    )
                }
            }
        }
    }
}
