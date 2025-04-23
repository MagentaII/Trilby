package com.example.trilby.ui.screens.auth.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.trilby.ui.screens.auth.login.LoginView
import kotlinx.serialization.Serializable

@Serializable
data object LoginRoute

fun NavController.navigateToLogin(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = LoginRoute) {
    navOptions()
}

fun NavGraphBuilder.loginGraph(
    onBackClick: () -> Unit,
) {
    composable<LoginRoute> {
        LoginView(
            onBackClick = onBackClick,
        )
    }
}