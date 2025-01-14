package com.example.trilby.ui.navigation.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.trilby.ui.TrilbyApp
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.screens.auth.login.LoginView
import com.example.trilby.ui.screens.auth.register.RegisterView
import com.example.trilby.ui.screens.splash.SplashView

@Composable
fun AuthNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Route.InApp,
    ) {
        composable<Route.Login> {
            LoginView(
                onNavigateToRegister = { route ->
                    navController.navigate(route)
                },
                onSignInNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable<Route.Register> {
            RegisterView(
                onNavigateToLogin = { route ->
                    navController.navigate(route)
                },
                onNavigate = { route ->
                    navController.navigate(route)
                }
            )
        }
        composable<Route.Splash> {
            SplashView(
                onNavigateToLogin = {},
                onNavigateToTrilby = {},
            )
        }
        navigation<Route.InApp>(startDestination = Route.Dictionary) {
            composable<Route.Dictionary> {
                TrilbyApp(
                    onNavigateToLogin = { route ->
                        navController.navigate(route = route)
                    }
                )
            }
        }
    }
}