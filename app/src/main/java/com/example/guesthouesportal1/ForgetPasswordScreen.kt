package com.example.guesthouesportal1

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

// ------------------------------ ForgotPasswordScreen ------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navigateToLogin: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
                    .imePadding()
                    .animateContentSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Bold, gradient header for "Forgot Password"
                Text(
                    text = "Forgot Password",
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

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        val lowerCaseEmail = it.lowercase()
                        email = lowerCaseEmail
                        emailError = if (lowerCaseEmail.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(lowerCaseEmail).matches())
                            "Invalid email" else null
                    },
                    label = { Text("Enter your Email") },
                    placeholder = { Text("example@mail.com") },
                    singleLine = true,
                    isError = emailError != null,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        autoCorrect = false,
                        capitalization = KeyboardCapitalization.None
                    ),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
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

                Button(
                    onClick = {
                        haptic.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                        if (email.isBlank()) {
                            emailError = "Email cannot be empty"
                            return@Button
                        }
                        keyboardController?.hide()
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "Password reset email sent!", Toast.LENGTH_SHORT).show()
                                    navigateToLogin()
                                } else {
                                    Toast.makeText(context, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Send Password Reset Email")
                }
                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = navigateToLogin,
                    modifier = Modifier.semantics { contentDescription = "Navigate to Login" }
                ) {
                    Text("Back to Login")
                }
            }
        }
    }
}


