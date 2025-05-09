package com.example.trilby.domain

import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.data.repositories.word_repository.WordRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val wordRepository: WordRepository,
) {
    suspend operator fun invoke(email: String, password: String) {
        authRepository.signIn(email, password)

    }
}