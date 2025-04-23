package com.example.trilby.ui.screens.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.trilby.ui.screens.favorites.FavoritesView
import kotlinx.serialization.Serializable

@Serializable
data object FavoritesRoute

@Serializable
data object FavoritesBaseRoute

fun NavController.navigateToFavorites(navOptions: NavOptions) = navigate(route = FavoritesRoute, navOptions = navOptions)

fun NavGraphBuilder.favoritesGraph(
    onItemClick: (String) -> Unit,
    detailDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<FavoritesBaseRoute>(startDestination = FavoritesRoute) {
        composable<FavoritesRoute> {
            FavoritesView(onItemClick = onItemClick)
        }
        detailDestination()
    }
}