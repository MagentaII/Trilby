package com.example.trilby.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trilby.ui.navigation.TrilbyNavHost
import com.example.trilby.ui.theme.TrilbyTheme
import com.example.trilby.ui.util.TrilbyBottomNavigationBar

@Composable
fun TrilbyApp(
    viewModel: TrilbyAppViewModel = hiltViewModel(),
) {
    // Navigation
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            if (currentDestination?.route in listOf(
                    "com.example.trilby.ui.navigation.Route.Dictionary",
                    "com.example.trilby.ui.navigation.Route.Profile",
                    "com.example.trilby.ui.navigation.Route.Practice",
                    "com.example.trilby.ui.navigation.Route.Favorites",
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
            modifier = Modifier.padding(innerPadding),
        )
    }
}

@Preview
@Composable
private fun TrilbyAppPreview() {
    TrilbyTheme {
        TrilbyApp()
    }
}