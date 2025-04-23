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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

//data class DictionaryUiState(
//    val words: List<ShowWord> = emptyList(),
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null,
//)

@HiltViewModel
class DictionaryViewModel @Inject constructor(
    private val wordRepository: WordRepository,
) : ViewModel() {
    // state
//    private val _uiState = MutableStateFlow(DictionaryUiState())
//    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()
    private val _uiState = MutableStateFlow<DictionaryUiState>(DictionaryUiState.Success(emptyList()))
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    fun changeSearchQuery(query: String) {
        searchQuery = query
    }

//    fun search(searchQuery: String) {
//        viewModelScope.launch {
//            _uiState.update { currentState ->
//                currentState.copy(
//                    isLoading = true
//                )
//            }
//            when (val result = wordRepository.search(searchQuery)) {
//                is Result.Success -> {
//                    _uiState.update { currentState ->
//                        currentState.copy(
//                            words = result.data,
//                            isLoading = false,
//                            errorMessage = null
//                        )
//                    }
//                }
//                is Result.Error -> {
//                    _uiState.update { currentState ->
//                        currentState.copy(
//                            words = emptyList(),
//                            isLoading = false,
//                            errorMessage = result.errorMessage
//                        )
//                    }
//                }
//            }
//        }
//    }

    fun searchWords(query: String) {
        if (searchQuery.isBlank()) return
        viewModelScope.launch {
            _uiState.value = DictionaryUiState.Loading
            wordRepository.searchWords(query)
                .catch { e ->
                    _uiState.value = DictionaryUiState.Error("發生錯誤: ${e.localizedMessage}")
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