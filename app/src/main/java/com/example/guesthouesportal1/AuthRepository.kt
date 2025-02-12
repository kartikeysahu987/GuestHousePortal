package com.example.guesthouesportal1

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("Users")

    suspend fun signUp(email: String, password: String, firstName: String, lastName: String, phoneNumber: String, gender: String): AuthState {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return AuthState.Error("User ID is null")

            // Creating user object
            val user = User(firstName, lastName, email, phoneNumber, gender)

            // Storing user data in Firebase Database
            database.child(userId).setValue(user).await()

            AuthState.Success("User registered successfully")
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Signup failed")
        }
    }

    suspend fun login(email: String, password: String): AuthState {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return AuthState.Error("User ID is null")

            // Retrieve user details from Firebase Database
            val userSnapshot = database.child(userId).get().await()
            val user = userSnapshot.getValue(User::class.java)

            // If user exists, return the name
            user?.let {
                AuthState.Success("Welcome ${it.firstName} ${it.lastName}")
            } ?: AuthState.Error("User data not found")
        } catch (e: Exception) {
            AuthState.Error(e.message ?: "Login failed")
        }
    }
}

// User Data Model
data class User(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val gender: String = ""
)

