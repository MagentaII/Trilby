package com.example.trilby.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.data.sources.network.auth_network_source.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val currentUser: User? = null,
    val showBottomSheet: Boolean = false,
    val isLoading: Boolean = false,
)

@HiltViewModel
open class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {
    // state
    private val _uiState = MutableStateFlow(ProfileUiState())
    open val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true
                )
            }
            repository.currentUser().collect { firebaseUser ->
                val user = repository.getUserInformation(firebaseUser?.uid)
                Log.i("Firestore", "observeCurrentUser: ${user?.name}")
                _uiState.update { currentState ->
                    currentState.copy(
                        currentUser = user,
                        isLoading = false,
                    )
                }
            }
        }
    }

    open fun signOut() {
        viewModelScope.launch {
            repository.signOut()
        }
    }

    fun showBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(
                showBottomSheet = true,
            )
        }
    }

    fun hideBottomSheet() {
        _uiState.update { currentState ->
            currentState.copy(
                showBottomSheet = false,
            )
        }
    }
}