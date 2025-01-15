package com.example.trilby.ui.screens.dictionary

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.data.repositories.word_repository.ShowWord
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.navigation.SharedViewModel
import com.example.trilby.ui.util.WordCard

@Composable
fun DictionaryView(
    viewModel: DictionaryViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel,
    words: List<ShowWord>,
    isLoading: Boolean = false,
    onNavigateToDetail: (route: Route, word: ShowWord) -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(words) {
        Log.i("TAG", "DictionaryView, words change: $words")
        viewModel.changeWords(words)
        sharedViewModel.updateWords(words)
    }

    if (words.isNotEmpty()) {
        Log.i("TAG", "DictionaryView, word: ${words[0]}")
    } else {
        Log.i("TAG", "DictionaryView, words list is empty")
    }

    val dictionaryUiState by viewModel.uiState.collectAsState()
    val showWords = dictionaryUiState.words
    Log.i("isLoading", "DictionaryView, isLoading: $isLoading")
    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(showWords) { word ->
                WordCard(word, onNavigateToDetail = onNavigateToDetail)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun DictionaryPreview() {
    DictionaryView(
        sharedViewModel = hiltViewModel(),
        words = emptyList(),
        onNavigateToDetail = { route, word ->
            Log.i("TAG", "DictionaryPreview, route, name: $route and ${word.uid}")
        }
    )
}



