package com.example.trilby.ui.screens.edit_profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.trilby.ui.screens.edit_profile.EditProfileView
import kotlinx.serialization.Serializable

@Serializable
data object EditProfileRoute

fun NavController.navigateToEditProfile(navOptions: NavOptionsBuilder.() -> Unit = {}) =
    navigate(route = EditProfileRoute) {
        navOptions()
    }

fun NavGraphBuilder.editProfileGraph(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    composable<EditProfileRoute> {
        EditProfileView(
            onCancelClick = onBackClick,
            onSaveClick = onBackClick
        )
    }
}