package com.example.trilby.ui.screens.auth.register

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.ui.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun updateConfirmPassword(newPassword: String) {
        confirmPassword = newPassword
    }

    fun onSignUpClick(
        onNavigate: (route: Route) -> Unit
    ) {
        viewModelScope.launch {
            Log.i("TAG", "onSignUpClick: Loading....")
            onNavigate(Route.Splash)
           val result = repository.signUp(email, password)
            if (result.isSuccess) {
                onNavigate(Route.Login)
            } else {
                onNavigate(Route.Register)
            }
        }
    }

}