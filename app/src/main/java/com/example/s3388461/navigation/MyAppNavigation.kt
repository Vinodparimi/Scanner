package com.example.s3388461.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.s3388461.pages.LoginPage
import com.example.s3388461.pages.PrivacyPolicyPage
import com.example.s3388461.pages.SplashScreen
import com.example.s3388461.viewmodels.AuthViewModel
import com.example.s3388461.pages.SignupPage

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            LoginPage(modifier = modifier, navController = navController, authViewModel = authViewModel)
        }
        composable("signup") {
            SignupPage(modifier = modifier, navController = navController, authViewModel = authViewModel)
        }
        composable("privacyPolicy") {
            PrivacyPolicyPage(navController = navController)
        }
    }
}
