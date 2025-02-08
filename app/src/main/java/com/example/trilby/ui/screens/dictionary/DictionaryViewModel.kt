package com.example.trilby.ui.screens.dictionary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trilby.data.repositories.word_repository.ShowWord
import com.example.trilby.data.repositories.word_repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DictionaryUiState(
    val words: List<ShowWord> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val wordRepository: WordRepository,
) : ViewModel() {
    // state
    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    fun changeSearchQuery(query: String) {
        searchQuery = query
    }

    fun search(searchQuery: String) {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            val words = wordRepository.search(searchQuery)
            wordRepository.search(searchQuery)
            _uiState.update { currentState ->
                currentState.copy(
                    words = words,
                    isLoading = false
                )
            }
        }
    }
}