package com.example.trilby.ui.navigation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trilby.ui.TrilbyApp
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.screens.login.LoginView
import com.example.trilby.ui.screens.register.RegisterView

@Composable
fun AuthNavHost(
    navController: NavHostController = rememberNavController(),
    isLoggedIn: Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Route.InApp else Route.Login,
    ) {
        composable<Route.Login> {
            LoginView(
                onNavigateToRegister = {
                    navController.navigate(Route.Register)
                }
            )
        }
        composable<Route.Register> {
            RegisterView(
                onNavigateToLogin = {
                    navController.navigate(Route.Login)
                }
            )
        }
        navigation<Route.InApp>(startDestination = Route.Dictionary) {
            composable<Route.Dictionary> {
                TrilbyApp()
            }
        }
    }
}