package com.example.trilby.ui.util

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.trilby.navigation.TopLevelDestination

@Composable
fun TrilbyBottomNavigationBar(
//    currentDestination: NavDestination?,
//    onNavigation: (route: Route) -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    colors: NavigationBarItemColors,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color(0xFF7988A9),
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = selected,
            onClick = onClick,
            icon = icon,
            label = label,
            colors = colors
        )
//        topLevelRoutes.forEach { topLevelRoute ->
//            NavigationBarItem(
//                selected = currentDestination?.hierarchy?.any {
//                    it.hasRoute(
//                        topLevelRoute.route::class
//                    )
//                } == true,
//                onClick = {
//                    Timber.d(
//                        "Current Destination Hierarchy: ${
//                        currentDestination?.hierarchy?.any {
//                            it.hasRoute(topLevelRoute.route::class)
//                        }
//                    }")
//                    if (currentDestination?.hierarchy?.any {
//                            it.hasRoute(
//                                topLevelRoute.route::class
//                            )
//                        } == false) {
//                        onNavigation(topLevelRoute.route)
//                    }
//                },
//                icon = {
//                    Icon(
//                        if (currentDestination?.hierarchy?.any {
//                                it.hasRoute(
//                                    topLevelRoute.route::class
//                                )
//                            } == true) {
//                            topLevelRoute.selectedIcon
//                        } else {
//                            topLevelRoute.unselectedIcon
//                        },
//                        contentDescription = topLevelRoute.name
//                    )
//                },
//                label = { Text(topLevelRoute.name) },
//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = Color(0xFFFCEFBC),
//                    unselectedIconColor = Color(0xFFFFFFFF),
//                    selectedTextColor = Color(0xFFFCEFBC),
//                    unselectedTextColor = Color(0xFFFFFFFF),
//                    indicatorColor = Color.Transparent,
//                )
//            )
    }
}


@Preview
@Composable
private fun TrilbyBottomNavigationBarPreview() {
//    val navController = rememberNavController()
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentDestination = navBackStackEntry?.destination
    TrilbyBottomNavigationBar(
        selected = true,
        onClick = {},
        icon = { Icon(TopLevelDestination.Dictionary.selectedIcon, contentDescription = null) },
        label = { Text(TopLevelDestination.Dictionary.iconTextId) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFFFCEFBC),
            unselectedIconColor = Color(0xFFFFFFFF),
            selectedTextColor = Color(0xFFFCEFBC),
            unselectedTextColor = Color(0xFFFFFFFF),
            indicatorColor = Color.Transparent,
        )

//        currentDestination = currentDestination,
//        onNavigation = { route->
//            Log.i("TAG", "route: $route")
//            navController.navigate(route) {
//                popUpTo(navController.graph.findStartDestination().id) {
//                    saveState = true
//                }
//                launchSingleTop = true
//                restoreState = true
//            }
//        }
    )
}