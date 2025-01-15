package com.example.trilby.ui

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

data class TrilbyAppUiState(
    val words: List<ShowWord> = emptyList(),
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
)

@HiltViewModel
class TrilbyAppViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TrilbyAppUiState())
    val uiState: StateFlow<TrilbyAppUiState> = _uiState.asStateFlow()

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
            val words = repository.search(searchQuery)
            repository.search(searchQuery)
            _uiState.update { currentState ->
                currentState.copy(
                    words = words,
                    isLoading = false
                )
            }
        }
    }

    fun saveWord(word: ShowWord) {
        viewModelScope.launch {
            repository.saveWord(word = word)
            isWordExist(word)
        }
    }

    fun deleteWord(word: ShowWord) {
        viewModelScope.launch {
            repository.deleteWord(word = word)
            isWordExist(word)
        }
    }

    fun isWordExist(word: ShowWord) {
        viewModelScope.launch {
            val isExist = repository.isWordExist(word = word)
            _uiState.update { currentState ->
                currentState.copy(
                    isFavorite = isExist
                )
            }
        }
    }
}