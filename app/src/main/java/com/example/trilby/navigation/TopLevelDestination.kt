package com.example.trilby.navigation

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
import com.example.trilby.ui.screens.dictionary.navigation.DictionaryBaseRoute
import com.example.trilby.ui.screens.dictionary.navigation.DictionaryRoute
import com.example.trilby.ui.screens.favorites.navigation.FavoritesBaseRoute
import com.example.trilby.ui.screens.favorites.navigation.FavoritesRoute
import com.example.trilby.ui.screens.practice.navigation.PracticeRoute
import com.example.trilby.ui.screens.profile.navigation.ProfileBaseRoute
import com.example.trilby.ui.screens.profile.navigation.ProfileRoute
import kotlin.reflect.KClass


enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: String,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route
) {
    Dictionary(
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.Outlined.Book,
        iconTextId = "Dictionary",
        route = DictionaryRoute::class,
        baseRoute = DictionaryBaseRoute::class,
    ),
    Favorites(
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.StarOutline,
        iconTextId = "Favorites",
        route = FavoritesRoute::class,
        baseRoute = FavoritesBaseRoute::class,
    ),
    Practice(
        selectedIcon = Icons.Filled.EditNote,
        unselectedIcon = Icons.Outlined.Edit,
        iconTextId = "Practice",
        route = PracticeRoute::class,
    ),
    Profile(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        iconTextId = "Profile",
        route = ProfileRoute::class,
        baseRoute = ProfileBaseRoute::class,
    ),
}