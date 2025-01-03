package com.example.trilby.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable


sealed class Route {
    @Serializable
    data object Login: Route()

    @Serializable
    data object Register: Route()

    @Serializable
    data object InApp: Route()

    @Serializable
    data object Dictionary : Route()

    @Serializable
    data object Favorites : Route()

    @Serializable
    data object Practice : Route()

    @Serializable
    data object Profile : Route()

    @Serializable
    data class WordDetail(
        val uid: String,
        val wordId: List<String>,
        val headword: List<String>,
        val label: List<String>,
        val definition: List<String>,
    ) : Route()
}

data class TopLevelRoute(
    val name: String,
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val topLevelRoutes = listOf(
    TopLevelRoute(
        name = "Dictionary",
        route = Route.Dictionary,
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.Book,
    ),
    TopLevelRoute(
        name = "Favorites",
        route = Route.Favorites,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.StarOutline,
    ),
    TopLevelRoute(
        name = "Practice",
        route = Route.Practice,
        selectedIcon = Icons.Filled.EditNote,
        unselectedIcon = Icons.Outlined.Edit,
    ),
    TopLevelRoute(
        name = "Profile",
        route = Route.Profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
)