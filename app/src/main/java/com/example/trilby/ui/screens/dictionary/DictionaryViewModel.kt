package com.example.trilby.ui.screens.dictionary

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trilby.data.repositories.word_repository.model.ShowWord
import com.example.trilby.data.repositories.word_repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val wordRepository: WordRepository,
) : ViewModel() {
    // state
    private val _uiState =
        MutableStateFlow<DictionaryUiState>(DictionaryUiState.Success(emptyList()))
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    fun changeSearchQuery(query: String) {
        searchQuery = query
    }

    fun searchWords() {
        if (searchQuery.isBlank()) return
        viewModelScope.launch {
            wordRepository.searchWords(searchQuery)
            wordRepository.getWords()
                .onStart { _uiState.value = DictionaryUiState.Loading }
                .catch { e ->
                    _uiState.value = DictionaryUiState.Error("搜尋單字失敗: ${e.localizedMessage}")
                }
                .collect { words ->
                    _uiState.value = DictionaryUiState.Success(words)
                }
        }
    }
}

sealed interface DictionaryUiState {
    data class Success(val words: List<ShowWord>) : DictionaryUiState
    data object Loading : DictionaryUiState
    data class Error(val errorMessage: String) : DictionaryUiState
}