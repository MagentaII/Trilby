package com.example.trilby.ui.screens.dictionary

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.trilby.data.sources.network.NetworkWords
import com.example.trilby.ui.navigation.Route
import com.example.trilby.ui.util.WordCard

@Composable
fun DictionaryView(
    viewModel: DictionaryViewModel = hiltViewModel(),
    words: List<NetworkWords> = emptyList(),
    onNavigateToDetail: (route: Route, name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    viewModel.changeWords(words)
    val dictionaryUiState by viewModel.uiState.collectAsState()
    val words = dictionaryUiState.words
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
private fun DictionaryPreview() {
    DictionaryView(
        words = emptyList(),
        onNavigateToDetail = { route, name ->
            Log.i("TAG", "DictionaryPreview, route, name: $route and $name")
        }
    )
}



