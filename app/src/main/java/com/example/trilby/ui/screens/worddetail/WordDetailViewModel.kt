package com.example.trilby.ui.screens.worddetail

import androidx.lifecycle.ViewModel
import com.example.trilby.data.repositories.Word
import com.example.trilby.data.repositories.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class WordDetailUiState(
    val words: List<Word> = emptyList()
)

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val repository: WordRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(WordDetailUiState())
    val uiState: StateFlow<WordDetailUiState> = _uiState.asStateFlow()

//    fun changeWord(id: String) {
//        val words = repository.words;
//        _uiState.update { currentState ->
//            currentState.copy(
//                words = words
//            )
//        }
//    }
}