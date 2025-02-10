package com.example.trilby.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.data.repositories.word_repository.ShowWord
import com.example.trilby.data.repositories.word_repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val savedWords: List<ShowWord> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val authRepository: AuthRepository

) : ViewModel() {
    // state
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

//    init {
//        fetchAllWordFromFirestore()
//        getAllSaveWords()
//    }

    fun getAllSaveWords() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }
            val userUid = authRepository.getCurrentUserUid()
            val words = wordRepository.fetchAllWordsToLocal(userUid = userUid)
            _uiState.update { currentState ->
                currentState.copy(
                    savedWords = words,
                    isLoading = false,
                )
            }
        }
    }

//    fun fetchAllWordFromFirestore() {
//        _uiState.update { currentState ->
//            currentState.copy(
//                isLoading = true,
//            )
//        }
//        viewModelScope.launch {
//            val showWords = repository.fetchAllWordFromFirestore()
//            val hasWords = repository.hasWords()
//            if (!hasWords) {
//                Log.i("Room", "There is no words in the room")
//                repository.saveAllWordsToLocal(showWords = showWords)
//            }
//            Log.d("FavoritesViewModel", "getFirestoreWord, showWords: $showWords")
//        }
//    }
}