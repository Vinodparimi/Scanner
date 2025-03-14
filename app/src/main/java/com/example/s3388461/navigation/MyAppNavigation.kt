package com.example.s3388461.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


import com.example.s3388461.pages.*
import com.example.s3388461.viewmodels.AuthViewModel

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object PrivacyPolicy : Screen("privacyPolicy")
    object Home : Screen("home")
    object Scanner : Screen("scannerPage")
}

@Composable
fun MyAppNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Splash.route) {

        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.Login.route) {
            LoginPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Screen.Signup.route) {
            SignupPage(
                modifier = modifier,
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(Screen.PrivacyPolicy.route) {
            PrivacyPolicyPage(navController = navController)
        }

        composable(Screen.Home.route) {
            HomePage(navController = navController)
        }

        composable(Screen.Scanner.route) {
            ScannerPage(navController = navController)
        }
    }
}
