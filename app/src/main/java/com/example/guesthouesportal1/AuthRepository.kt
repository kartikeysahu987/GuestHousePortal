package com.example.guesthouesportal1

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    suspend fun signUp(email: String, password: String): AuthState {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            AuthState.Success("Sign-up successful")
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun login(email: String, password: String): AuthState {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthState.Success("Login successful")
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Login failed")
        }
    }
}
