package com.example.trilby.ui.screens.word_detail.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.trilby.ui.screens.word_detail.WordDetailView
import com.example.trilby.ui.screens.word_detail.WordDetailViewModel
import kotlinx.serialization.Serializable

@Serializable
data class WordDetailRoute(val id: String)

fun NavController.navigateToWordDetail(
    wordId: String,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route = WordDetailRoute(wordId)) {
        navOptions()
    }
}

fun NavGraphBuilder.wordDetailGraph(
    onBackClick: () -> Unit,
) {
    composable<WordDetailRoute> { entry ->
        val id = entry.toRoute<WordDetailRoute>().id
        WordDetailView(
            onBackClick = onBackClick,
            viewModel = hiltViewModel<WordDetailViewModel, WordDetailViewModel.Factory>(
                key = id
            ) { factory ->
                factory.create(id)
            }
        )
    }
}