package com.example.trilby.data.repositories.auth_repository

import com.example.trilby.data.sources.network.auth_network_source.AuthService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AuthRepository {
    fun currentUser(): Flow<FirebaseUser?>
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Result<FirebaseUser?>
    suspend fun signUp(email: String, password: String): Result<FirebaseUser?>
    suspend fun signOut()
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override fun currentUser(): Flow<FirebaseUser?> {
        return authService.currentUser()
    }

    override fun hasUser(): Boolean {
        return authService.hasUser()
    }

    override suspend fun signIn(email: String, password: String): Result<FirebaseUser?> {
        val result = authService.signIn(email, password)
        return result
    }

    override suspend fun signUp(email: String, password: String): Result<FirebaseUser?> {
        val result = authService.signUp(email, password)
        return result
    }

    override suspend fun signOut() {
        authService.signOut()
    }
}