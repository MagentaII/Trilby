package com.example.trilby.ui.screens.practice.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.example.trilby.ui.screens.practice.PracticeView
import kotlinx.serialization.Serializable

@Serializable
data object PracticeRoute

fun NavController.navigateToPractice(navOptions: NavOptions) = navigate(route = PracticeRoute, navOptions = navOptions)

fun NavGraphBuilder.practiceGraph() {
    composable<PracticeRoute> {
        PracticeView()
    }
}