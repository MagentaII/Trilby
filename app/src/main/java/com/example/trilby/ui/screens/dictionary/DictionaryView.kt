package com.example.trilby.ui.screens.dictionary

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.navigation.SharedViewModel
import com.example.trilby.ui.util.AddSearchBarTopAppBar
import com.example.trilby.ui.util.WordCard

@Composable
fun DictionaryView(
    viewModel: DictionaryViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    onNavigateToDetail: (route: Route) -> Unit,
    modifier: Modifier = Modifier
) {
    val dictionaryUiState by viewModel.uiState.collectAsState()
    val showWords = dictionaryUiState.words
    val isLoading = dictionaryUiState.isLoading

    LaunchedEffect(showWords) {
        Log.i("TAG", "DictionaryView, words change: $showWords")
        sharedViewModel.updateWords(showWords)
    }

    if (showWords.isNotEmpty()) {
        Log.i("TAG", "DictionaryView, word: ${showWords[0]}")
    } else {
        Log.i("TAG", "DictionaryView, words list is empty")
    }
    Log.i("isLoading", "DictionaryView, isLoading: $isLoading")

    Scaffold(
        topBar = {
            AddSearchBarTopAppBar(
                viewModel = viewModel,
                title = "Dictionary"
            )
        },
    ) { innerPadding ->
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(showWords) { word ->
                    WordCard(word, onNavigateToDetail = onNavigateToDetail)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DictionaryPreview() {
    DictionaryView(
        sharedViewModel = hiltViewModel(),
        onNavigateToDetail = { route ->
            Log.i("TAG", "DictionaryPreview, route, name: $route and")
        }
    )
}



