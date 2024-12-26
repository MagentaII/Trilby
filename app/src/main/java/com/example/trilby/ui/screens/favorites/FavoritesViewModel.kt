package com.example.trilby.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trilby.data.repositories.Word
import com.example.trilby.data.repositories.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val savedWords: List<Word> = emptyList()
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {
    // state
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun getAllSaveWords() {
        viewModelScope.launch {
            val words = repository.getAllWords()
            _uiState.update { currentState ->
                currentState.copy(
                    savedWords = words
                )
            }
        }
    }

//    fun changeSavedWords() {
//        val words = repository.getWords();
//        _uiState.update { currentState ->
//            currentState.copy(
//                savedWords = words
//            )
//        }
//    }
}