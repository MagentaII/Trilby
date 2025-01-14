package com.example.trilby.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.trilby.data.repositories.word_repository.ShowWord
import com.example.trilby.ui.TrilbyAppUiState
import com.example.trilby.ui.screens.dictionary.DictionaryView
import com.example.trilby.ui.screens.favorites.FavoritesView
import com.example.trilby.ui.screens.practice.PracticeView
import com.example.trilby.ui.screens.profile.ProfileView
import com.example.trilby.ui.screens.worddetail.WordDetailView

@Composable
fun TrilbyNavHost(
    trilbyAppUiState: TrilbyAppUiState,
    navController: NavHostController,
    onNavigateToDetail: (route: Route, word: ShowWord) -> Unit,
    onNavigateToLogin: (route: Route) -> Unit,
    sharedViewModel: SharedViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    NavHost(
        navController = navController,
        startDestination = Route.Dictionary,
        modifier
    ) {
        composable<Route.Dictionary> {
            DictionaryView(
                words = trilbyAppUiState.words,
                onNavigateToDetail = onNavigateToDetail,
                sharedViewModel = sharedViewModel,
            )
        }
        composable<Route.Favorites> {
            FavoritesView(
                onNavigateToDetail = onNavigateToDetail,
                sharedViewModel = sharedViewModel,
            )
        }
        composable<Route.Practice> { PracticeView() }
        composable<Route.Profile> {
            ProfileView(
                onNavigateToLogin = onNavigateToLogin
            )
        }
        composable<Route.WordDetail> { backStateEntry ->
            val wordDetail: Route.WordDetail = backStateEntry.toRoute()
            val uiState by sharedViewModel.uiState.collectAsState()
            Log.i("TAG", "TrilbyNavHost, words change: ${uiState.words}")
            WordDetailView(
                wordDetail = wordDetail,
                words = uiState.words,
            )
        }
    }
}