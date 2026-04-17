package com.example.dualstride.ui.navigation

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.*
import com.example.dualstride.ui.auth.AuthViewModel
import com.example.dualstride.ui.auth.LoginScreen
import com.example.dualstride.ui.auth.RegisterScreen
import com.example.dualstride.ui.home.HomeScreen

sealed class Screen(val route: String) {
    object Login    : Screen("login")
    object Register : Screen("register")
    object Home     : Screen("home")
}

@Composable
fun GuardianNavGraph() {
    val navController = rememberNavController()
    val authViewModel : AuthViewModel = hiltViewModel()
    val isLoggedIn    by authViewModel.isLoggedIn.collectAsStateWithLifecycle()

    NavHost(
        navController    = navController,
        startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}