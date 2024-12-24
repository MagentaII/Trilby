package com.example.trilby.ui

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.navigation.TrilbyNavHost
import com.example.trilby.ui.theme.TrilbyTheme
import com.example.trilby.ui.util.AddSearchBarTopAppBar
import com.example.trilby.ui.util.DefaultTopAppBar
import com.example.trilby.ui.util.DetailTopAppBar
import com.example.trilby.ui.util.TrilbyBottomNavigationBar

@Composable
fun TrilbyApp(
    viewModel: TrilbyAppViewModel = hiltViewModel(),
) {
    // Navigation
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // UIState
    val trilbyAppUiState by viewModel.uiState.collectAsState()

    var currentRoute by remember { mutableStateOf<Route>(Route.Dictionary) }
    var title by remember { mutableStateOf("Dictionary") }
    var onNavigationTitle by remember { mutableStateOf("") }

    LaunchedEffect(currentDestination) {
        currentDestination?.route?.let { route ->
            Log.i("TAG", "TrilbyApp, Current Route: $route")
            val formattedRoute = formatRoute(route)
            formattedRoute?.let { formatted ->
                when (formatted) {
                    Route.Dictionary -> {
                        currentRoute = Route.Dictionary
                        title = "Dictionary"
                    }

                    Route.Favorites -> {
                        currentRoute = Route.Favorites
                        title = "Favorites"
                    }

                    Route.Practice -> {
                        currentRoute = Route.Practice
                        title = "Practice"
                    }

                    Route.Profile -> {
                        currentRoute = Route.Profile
                        title = "Profile"
                    }

                    is Route.WordDetail -> {
                        Log.i("TAG", "TrilbyApp, Route.WordDetail: ")
                        currentRoute = Route.WordDetail(id = "")
                        title = onNavigationTitle
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            when (currentRoute) {
                is Route.Dictionary -> AddSearchBarTopAppBar(
                    viewModel = viewModel,
                    title = title
                )

                is Route.WordDetail -> DetailTopAppBar(
                    title = title,
                    onPopBack = { navController.popBackStack() }
                )

                else -> DefaultTopAppBar(
                    title = title,
                )
            }
        },
        bottomBar = {
            if (currentRoute in listOf(
                    Route.Dictionary,
                    Route.Favorites,
                    Route.Practice,
                    Route.Profile
                )
            ) {
                TrilbyBottomNavigationBar(
                    currentDestination = currentDestination,
                    onNavigation = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
    ) { innerPadding ->
        TrilbyNavHost(
            navController = navController,
            trilbyAppUiState = trilbyAppUiState,
            modifier = Modifier.padding(innerPadding),
            onNavigation = { route, name ->
                onNavigationTitle = name
                navController.navigate(route)
            },
        )
    }
}

fun formatRoute(route: String): Route? {
    return when {
        route.endsWith("Route.Dictionary") -> Route.Dictionary
        route.endsWith("Route.Favorites") -> Route.Favorites
        route.endsWith("Route.Practice") -> Route.Practice
        route.endsWith("Route.Profile") -> Route.Profile
        route.endsWith("Route.WordDetail/{id}") -> Route.WordDetail(id = "")
        else -> null // 未知路由返回 null
    }
}


@Preview
@Composable
private fun TrilbyAppPreview() {
    TrilbyTheme {
        TrilbyApp()
    }
}