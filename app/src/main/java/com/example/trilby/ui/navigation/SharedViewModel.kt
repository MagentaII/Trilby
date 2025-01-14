package com.example.trilby.ui.navigation

import androidx.lifecycle.ViewModel
import com.example.trilby.data.repositories.word_repository.ShowWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class SharedUiState(
    val words: List<ShowWord> = emptyList()
)

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState: StateFlow<SharedUiState> = _uiState.asStateFlow()

    fun updateWords(newWords: List<ShowWord>) {
        _uiState.update { currentState ->
            currentState.copy(
                words = newWords
            )
        }
    }
}