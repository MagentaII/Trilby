package com.example.trilby.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
object Dictionary

@Serializable
object Favorites

@Serializable
object Practice

@Serializable
object Profile

data class TopLevelRoute<T : Any>(
    val name: String,
    val route: T,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val showSearchBar: Boolean
)

val topLevelRoutes = listOf(
    TopLevelRoute(
        name = "Dictionary",
        route = Dictionary,
        selectedIcon = Icons.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.Book,
        showSearchBar = true
    ),
    TopLevelRoute(
        name = "Favorites",
        route = Favorites,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.StarOutline,
        showSearchBar = false
    ),
    TopLevelRoute(
        name = "Practice",
        route = Practice,
        selectedIcon = Icons.Filled.EditNote,
        unselectedIcon = Icons.Outlined.Edit,
        showSearchBar = false
    ),
    TopLevelRoute(
        name = "Profile",
        route = Profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        showSearchBar = false
    ),
)