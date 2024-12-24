package com.example.trilby.ui.util

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.navigation.topLevelRoutes

@Composable
fun TrilbyBottomNavigationBar(
    currentDestination: NavDestination?,
    onNavigation: (route: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color(0xFF7988A9),
        modifier = modifier
    ) {
        topLevelRoutes.forEach { topLevelRoute ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any {
                    it.hasRoute(
                        topLevelRoute.route::class
                    )
                } == true,
                onClick = {
                    Log.i(
                        "TAG",
                        "Current Destination Hierarchy: ${
                            currentDestination?.hierarchy?.any {
                                it.hasRoute(topLevelRoute.route::class)
                            }
                        }"
                    )
                    if (currentDestination?.hierarchy?.any {
                            it.hasRoute(
                                topLevelRoute.route::class
                            )
                        } == false) {
                        onNavigation(topLevelRoute.route)
                    }
                },
                icon = {
                    Icon(
                        if (currentDestination?.hierarchy?.any {
                                it.hasRoute(
                                    topLevelRoute.route::class
                                )
                            } == true) {
                            topLevelRoute.selectedIcon
                        } else {
                            topLevelRoute.unselectedIcon
                        },
                        contentDescription = topLevelRoute.name
                    )
                },
                label = { Text(topLevelRoute.name) },
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

@Preview
@Composable
private fun TrilbyBottomNavigationBarPreview() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    TrilbyBottomNavigationBar(
//        navController = navController,
        currentDestination = currentDestination,
        onNavigation = { route->
            Log.i("TAG", "route: $route")
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