package com.example.trilby.ui.screens.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.trilby.ui.screens.profile.ProfileView
import kotlinx.serialization.Serializable

@Serializable
data object ProfileRoute

@Serializable
data object ProfileBaseRoute

fun NavController.navigateToProfile(navOptions: NavOptions) =
    navigate(route = ProfileRoute, navOptions = navOptions)

fun NavGraphBuilder.profileGraph(
    onEditProfileClick: () -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    editProfileDestination: NavGraphBuilder.() -> Unit,
    loginDestination: NavGraphBuilder.() -> Unit,
    registerDestination: NavGraphBuilder.() -> Unit
) {
    navigation<ProfileBaseRoute>(startDestination = ProfileRoute) {
        composable<ProfileRoute> {
            ProfileView(
                onEditProfileClick = onEditProfileClick,
                onLoginClick = onLoginClick,
                onRegisterClick = onRegisterClick
            )
        }
        editProfileDestination()
        loginDestination()
        registerDestination()
    }
}