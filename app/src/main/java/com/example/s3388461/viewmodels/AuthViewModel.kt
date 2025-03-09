package com.example.s3388461.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    init {
        try {
            FirebaseApp.getInstance()
            checkCurrentUser()
        } catch (e: Exception) {
            Log.e("AuthViewModel", "Firebase is not initialized: ${e.message}")
            _authState.value = AuthState.Error("Firebase is not initialized. Restart the app.")
        }
    }

    private fun checkCurrentUser() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            _authState.value = AuthState.Authenticated(user)
        } else {
            _authState.value = AuthState.Unauthenticated
        }
    }

    fun signup(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be blank")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                    Log.d("AuthViewModel", "Signup successful: ${user.email}")
                } else {
                    _authState.value = AuthState.Error("Signup failed. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Signup failed: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Signup failed. Please try again.")
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password cannot be blank")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                if (user != null) {
                    _authState.value = AuthState.Authenticated(user)
                    Log.d("AuthViewModel", "Login successful: ${user.email}")
                } else {
                    _authState.value = AuthState.Error("Login failed. Please try again.")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login failed: ${e.message}")
                _authState.value = AuthState.Error(e.message ?: "Login failed. Please try again.")
            }
        }
    }

    fun signout() {
        firebaseAuth.signOut()
        _authState.value = AuthState.Unauthenticated
        Log.d("AuthViewModel", "User signed out")
    }
}

sealed class AuthState {
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Authenticated(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}