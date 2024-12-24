package com.example.trilby.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trilby.data.repositories.WordRepository
import com.example.trilby.data.sources.network.NetworkWords
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TrilbyAppUiState(
    val words: List<NetworkWords> = emptyList()
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

    fun search() {
        viewModelScope.launch {
            val words = repository.search(searchQuery)
            _uiState.update { currentState ->
                currentState.copy(
                    words = words
                )
            }
        }
    }
}