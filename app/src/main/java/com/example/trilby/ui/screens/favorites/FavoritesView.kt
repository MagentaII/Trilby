package com.example.trilby.ui.screens.favorites

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.navigation.SharedViewModel
import com.example.trilby.ui.util.DefaultTopAppBar
import com.example.trilby.ui.util.WordCard

@Composable
fun FavoritesView(
    viewModel: FavoritesViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    onNavigateToDetail: (route: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoritesUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val words = favoritesUiState.savedWords
    val isLoading = favoritesUiState.isLoading

    LaunchedEffect(words) {
        Log.i("TAG", "FavoritesView, words change: $words")
        sharedViewModel.updateWords(words)
    }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = "Favorites",
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                items(words) { word ->
                    WordCard(word, onNavigateToDetail = onNavigateToDetail)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesPreview() {
    FavoritesView(
        onNavigateToDetail = { route ->
            Log.i("TAG", "FavoritesPreview, route, name: $route")
        },
        sharedViewModel = hiltViewModel()
    )
}