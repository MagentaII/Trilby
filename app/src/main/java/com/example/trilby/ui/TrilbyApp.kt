package com.example.trilby.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.example.trilby.navigation.TrilbyNavHost
import com.example.trilby.ui.theme.TrilbyTheme
import kotlin.reflect.KClass

@Composable
fun TrilbyApp() {
    // AppState
    val appState = rememberTrilbyAppState()
    // Navigation
    val navController = appState.navController
    val currentDestination = appState.currentDestination
    Scaffold(
        bottomBar = {
            val currentTopLevelDestination = appState.currentTopLevelDestination
            if (currentTopLevelDestination != null)
                NavigationBar(
                    containerColor = Color(0xFF7988A9),
                ) {
                    appState.topLevelDestination.forEach { destination ->
                        val selected = currentDestination.isRouteInHierarchy(destination.baseRoute)

                        NavigationBarItem(
                            selected = selected,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = {
                                Icon(
                                    imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(destination.iconTextId) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFFFCEFBC),
                                unselectedIconColor = Color(0xFFFFFFFF),
                                selectedTextColor = Color(0xFFFCEFBC),
                                unselectedTextColor = Color(0xFFFFFFFF),
                                indicatorColor = Color.Transparent,
                            )
                        )
                    }
                }
        }
    ) { innerPadding ->
        TrilbyNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

private fun NavDestination?.isRouteInHierarchy(route: KClass<*>): Boolean {
    return this?.hierarchy?.any {
        it.hasRoute(route)
    } ?: false
}

@Preview
@Composable
private fun TrilbyAppPreview() {
    TrilbyTheme {
        TrilbyApp()
    }
}