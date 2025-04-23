package com.example.trilby.ui.screens.auth.register.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.trilby.ui.screens.auth.register.RegisterView
import kotlinx.serialization.Serializable

@Serializable
data object RegisterRoute

fun NavController.navigateToRegister(
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(route = RegisterRoute) {
    navOptions()
}

fun NavGraphBuilder.registerGraph(
    onBackClick: () -> Unit,
) {
    composable<RegisterRoute> {
        RegisterView(
            onBackClick = onBackClick,
        )
    }
}