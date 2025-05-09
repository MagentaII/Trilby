package com.example.trilby.ui.screens.dictionary

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.trilby.ui.util.AddSearchBarTopAppBar
import com.example.trilby.ui.util.WordCard
import timber.log.Timber

@Composable
fun DictionaryView(
    viewModel: DictionaryViewModel = hiltViewModel(),
//    onUpdateSharedWords: (List<ShowWord>) -> Unit,
//    onNavigateToDetail: (route: Route) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val dictionaryUiState by viewModel.uiState.collectAsStateWithLifecycle()

//    LaunchedEffect(dictionaryUiState.words) {
//        Timber.d("words change: $dictionaryUiState.words")
//        onUpdateSharedWords(dictionaryUiState.words)
//    }

    Scaffold(
        topBar = {
            AddSearchBarTopAppBar(
                title = "Dictionary",
                query = viewModel.searchQuery,
                onValueChange = { query ->
                    viewModel.changeSearchQuery(query)
                },
                onSearch = { viewModel.searchWords() }
            )
        },
    ) { innerPadding ->

        when (val state = dictionaryUiState) {
            is DictionaryUiState.Loading -> {
                Timber.d("UI state is loading")
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }

            is DictionaryUiState.Success -> {
                LazyColumn(
                    modifier = modifier.padding(innerPadding)
                ) {
                    items(state.words) { word ->
                        WordCard(word, onItemClick = onItemClick)
                    }
                }
            }

            is DictionaryUiState.Error -> {
                Timber.d("UI state error message: ${state.errorMessage}")
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = state.errorMessage,
                        textAlign = TextAlign.Center
                    )
                }
            }

//            dictionaryUiState.isLoading -> {
//                // Show Loading indicator
//                Timber.d("UI state is loading")
//                Box(
//                    contentAlignment = Alignment.Center,
//                    modifier = Modifier
//                        .padding(innerPadding)
//                        .fillMaxSize()
//                ) {
//                    LinearProgressIndicator(
//                        modifier = Modifier.padding(horizontal = 16.dp),
//                        color = MaterialTheme.colorScheme.secondary,
//                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
//                    )
//                }
//            }

//            dictionaryUiState.errorMessage != null -> {
//                // Show Error message
//                Timber.d("UI state error message: ${dictionaryUiState.errorMessage}")
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize(),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = dictionaryUiState.errorMessage ?: "發生錯誤",
//                        textAlign = TextAlign.Center
//                    )
//                }
//            }

//            dictionaryUiState.words.isNotEmpty() -> {
//                LazyColumn(
//                    modifier = modifier.padding(innerPadding)
//                ) {
//                    items(dictionaryUiState.words) { word ->
//                        WordCard(word, onItemClick = onItemClick)
//                    }
//                }
//            }
        }
    }
}


@SuppressLint("LogNotTimber")
@Preview(showBackground = true)
@Composable
private fun DictionaryPreview() {
    DictionaryView(
//        onUpdateSharedWords = {},
        onItemClick = {},
    )
}



