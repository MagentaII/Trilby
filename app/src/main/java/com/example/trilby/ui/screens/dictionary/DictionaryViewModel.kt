package com.example.trilby.ui.screens.dictionary

import androidx.lifecycle.ViewModel
import com.example.trilby.data.repositories.ShowWord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class DictionaryUiState(
    val words: List<ShowWord> = emptyList()
)

@HiltViewModel
class DictionaryViewModel @Inject constructor(
//    private val repository: WordRepository
) : ViewModel() {
    // state
    private val _uiState = MutableStateFlow(DictionaryUiState())
    val uiState: StateFlow<DictionaryUiState> = _uiState.asStateFlow()

    fun changeWords(words: List<ShowWord>) {
//        val words = repository.searchWords;
        _uiState.update { currentState ->
            currentState.copy(
                words = words
            )
        }
    }

//    fun changeWords(words: List<Word>) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                words = words
//            )
//        }
//    }
}