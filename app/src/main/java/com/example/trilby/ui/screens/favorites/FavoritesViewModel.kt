package com.example.trilby.ui.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.data.repositories.word_repository.ShowWord
import com.example.trilby.data.repositories.word_repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
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
    val uiState: StateFlow<FavoritesUiState> = _uiState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            FavoritesUiState()
        )

    init {
        observeUserUid()
    }

    private fun observeUserUid() {
        viewModelScope.launch {
            val storeUserUid = authRepository.getUserUid()
            authRepository.getCurrentUserUid().collectLatest { userUid ->
                if (userUid == null) {
                    wordRepository.deleteAllWordsForLocal()
                    authRepository.saveUserUid(userUid)
                }
                if (storeUserUid != userUid) {
                    Log.i("TAG", "observeUserUid: user change")
                    wordRepository.deleteAllWordsForLocal()
                    authRepository.saveUserUid(userUid)
                }
                Log.i("TAG", "observeUserUid: observeWords")
                observeWords(userUid = userUid)
            }
        }
    }

    private fun observeWords(userUid: String?) {
        Log.i("TAG", "fetchAllWordsToLocal: observeWords: ")
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                )
            }
            wordRepository.fetchAllWordsToLocal(userUid = userUid)
                .collectLatest { words ->
                    _uiState.update { currentState ->
                        currentState.copy(
                            savedWords = words,
                            isLoading = false,
                        )
                    }
                }

        }
    }
}