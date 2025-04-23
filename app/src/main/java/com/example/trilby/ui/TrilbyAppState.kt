package com.example.trilby.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.example.trilby.navigation.TopLevelDestination
import com.example.trilby.navigation.TopLevelDestination.Dictionary
import com.example.trilby.navigation.TopLevelDestination.Favorites
import com.example.trilby.navigation.TopLevelDestination.Practice
import com.example.trilby.navigation.TopLevelDestination.Profile
import com.example.trilby.ui.screens.dictionary.navigation.navigateToDictionary
import com.example.trilby.ui.screens.favorites.navigation.navigateToFavorites
import com.example.trilby.ui.screens.practice.navigation.navigateToPractice
import com.example.trilby.ui.screens.profile.navigation.navigateToProfile

@Composable
fun rememberTrilbyAppState(
    navController: NavHostController = rememberNavController(),
): TrilbyAppState {
    return remember(navController) {
        TrilbyAppState(
            navController = navController,
        )
    }
}

@Stable
class TrilbyAppState(
    val navController: NavHostController,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            // Collect the currentBackStackEntryFlow as a state
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }


    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            return TopLevelDestination.entries.firstOrNull { topLevelDestination ->
                currentDestination?.hasRoute(route = topLevelDestination.route) == true
            }
        }


    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestination: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOption = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            Dictionary -> navController.navigateToDictionary(topLevelNavOption)
            Favorites -> navController.navigateToFavorites(topLevelNavOption)
            Practice -> navController.navigateToPractice(topLevelNavOption)
            Profile -> navController.navigateToProfile(topLevelNavOption)
        }
    }
}

