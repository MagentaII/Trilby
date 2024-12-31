package com.example.trilby.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.trilby.data.repositories.ShowWord
import com.example.trilby.ui.TrilbyAppUiState
import com.example.trilby.ui.screens.dictionary.DictionaryView
import com.example.trilby.ui.screens.favorites.FavoritesView
import com.example.trilby.ui.screens.practice.PracticeView
import com.example.trilby.ui.screens.profile.ProfileView
import com.example.trilby.ui.screens.worddetail.WordDetailView

@Composable
fun TrilbyNavHost(
    trilbyAppUiState: TrilbyAppUiState,
    navController: NavHostController = rememberNavController(),
    onNavigation: (route: Route, word: ShowWord) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Dictionary,
        modifier
    ) {
        composable<Route.Dictionary> {
            DictionaryView(
                words = trilbyAppUiState.words,
                onNavigateToDetail = onNavigation
            )
        }
        composable<Route.Favorites> {
            FavoritesView(
                onNavigateToDetail = onNavigation
            )
        }
        composable<Route.Practice> { PracticeView() }
        composable<Route.Profile> { ProfileView() }
        composable<Route.WordDetail> { backStateEntry ->
            val wordDetail: Route.WordDetail = backStateEntry.toRoute()
            WordDetailView(
                wordDetail = wordDetail,
//                words = trilbyAppUiState.words,
            )
        }
    }
}