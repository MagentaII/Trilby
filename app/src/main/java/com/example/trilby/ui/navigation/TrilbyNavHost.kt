package com.example.trilby.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.trilby.ui.screens.dictionary.DictionaryView
import com.example.trilby.ui.screens.favorites.FavoritesView
import com.example.trilby.ui.screens.practice.PracticeView
import com.example.trilby.ui.screens.profile.ProfileView

@Composable
fun TrilbyNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Dictionary,
        modifier
    ) {
        composable<Dictionary> { DictionaryView() }
        composable<Favorites> { FavoritesView() }
        composable<Practice> { PracticeView() }
        composable<Profile> { ProfileView() }
    }
}