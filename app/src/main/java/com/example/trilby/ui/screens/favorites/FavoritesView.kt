package com.example.trilby.ui.screens.favorites

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.data.repositories.Word
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.util.WordCard

@Composable
fun FavoritesView(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onNavigateToDetail: (route: Route, word: Word) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoritesUiState by viewModel.uiState.collectAsState()
    val words = favoritesUiState.savedWords
    LaunchedEffect(Unit) {
        viewModel.getAllSaveWords()
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        items(words) { word ->
            WordCard(word, onNavigateToDetail = onNavigateToDetail)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesPreview() {
    FavoritesView(
        onNavigateToDetail = { route, word ->
            Log.i("TAG", "FavoritesPreview, route, name: $route and ${word.headword}")
        }
    )
}