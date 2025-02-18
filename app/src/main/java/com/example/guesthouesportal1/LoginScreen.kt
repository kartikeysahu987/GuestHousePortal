package com.example.guesthouesportal1

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.scale
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
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navigateToSignUp: () -> Unit,
    navigateToForgotPassword: () -> Unit,
    navController: NavController
) {
    // -- (1) Check if user is already logged in. If so, skip login screen.
    LaunchedEffect(Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            navController.navigate("main_screen") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

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

    // -- (2) If login is successful, navigate to main screen.
    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Loading -> {
                // Optionally show a loader or disable UI
            }
            is AuthState.Success -> {
                val message = (authState as AuthState.Success).message
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                navController.navigate("main_screen") {
                    popUpTo("login") { inclusive = true }
                }
            }
            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).error, Toast.LENGTH_SHORT).show()
            }
            else -> { /* Idle or other states */ }
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
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f),
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Guest House Portal", style = MaterialTheme.typography.titleLarge) },
                    modifier = Modifier.shadow(4.dp)
                )
            }
        ) { paddingValues ->
            // Animate the entire form sliding in from top & fading in
            var formVisible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { formVisible = true }

            AnimatedVisibility(
                visible = formVisible,
                enter = slideInVertically(
                    initialOffsetY = { -100 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) + fadeIn(animationSpec = spring()),
                exit = slideOutVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp)
                        .imePadding() // Adapts to the keyboard
                        .animateContentSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Animated "Login" header
                    var headerScaleValue by remember { mutableStateOf(0.8f) }
                    LaunchedEffect(Unit) {
                        headerScaleValue = 1f
                    }
                    val headerScale by animateFloatAsState(
                        targetValue = headerScaleValue,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.scale(headerScale)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Email Field
                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            val lowerCaseEmail = it.lowercase()
                            email = lowerCaseEmail
                            emailError = if (
                                !android.util.Patterns.EMAIL_ADDRESS.matcher(lowerCaseEmail).matches() &&
                                lowerCaseEmail.isNotEmpty()
                            ) {
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

                    // Password Field
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
                                interactionSource = MutableInteractionSource()
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

                    // Login Button
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(
                                androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress
                            )
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
                        enabled = authState !is AuthState.Loading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary,
                            disabledContentColor = MaterialTheme.colorScheme.onPrimary
                        )
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

                    // Navigation: Sign Up & Forgot Password
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
}
