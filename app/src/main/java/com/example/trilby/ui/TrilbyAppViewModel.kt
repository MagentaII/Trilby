package com.example.trilby.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class TrilbyAppUiState(
 val temp: String = ""
)

@HiltViewModel
class TrilbyAppViewModel @Inject constructor(
) : ViewModel() {
    private val _uiState = MutableStateFlow(TrilbyAppUiState())
    val uiState: StateFlow<TrilbyAppUiState> = _uiState.asStateFlow()

}