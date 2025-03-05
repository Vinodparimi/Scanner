package com.example.s3388461

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.s3388461.navigation.MyAppNavigation
import com.example.s3388461.ui.theme.ScannerTheme
import com.example.s3388461.viewmodels.AuthViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        setContent {
            // Initialize ViewModel for authentication
            val authViewModel: AuthViewModel = viewModel()

            // Apply the app theme
            ScannerTheme {  // Use the correct theme function
                // Start the app navigation and pass ViewModel
                MyAppNavigation(authViewModel = authViewModel)
            }
        }
    }
}
