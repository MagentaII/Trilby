package com.example.trilby.domain

import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.data.repositories.word_repository.WordRepository
import javax.inject.Inject


/**
 * 登入用例
 * @param authRepository
 * @param wordRepository
 * 1. 登入
 * 2. 刪除本地資料
 * 3. 將遠端資料匯入到本地
 */
class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val wordRepository: WordRepository,
) {
    suspend operator fun invoke(email: String, password: String) {
        authRepository.signIn(email, password)
        wordRepository.getNetworkWords()
    }
}