package com.example.trilby.ui.screens.dictionary.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.trilby.ui.screens.dictionary.DictionaryView
import kotlinx.serialization.Serializable

@Serializable
data object DictionaryRoute

@Serializable
data object DictionaryBaseRoute

fun NavController.navigateToDictionary(navOptions: NavOptions) = navigate(route = DictionaryRoute, navOptions = navOptions)

fun NavGraphBuilder.dictionaryGraph(
    onItemClick: (String) -> Unit,
    detailDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<DictionaryBaseRoute>(startDestination = DictionaryRoute) {
        composable<DictionaryRoute> {
            DictionaryView(onItemClick = onItemClick)
        }
        detailDestination()
    }
}