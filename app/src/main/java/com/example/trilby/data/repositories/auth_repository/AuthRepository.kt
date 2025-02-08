package com.example.trilby.data.repositories.auth_repository

import android.util.Log
import com.example.trilby.data.sources.network.auth_network_source.AuthService
import com.example.trilby.data.sources.network.auth_network_source.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    fun currentUser(): Flow<FirebaseUser?>
    suspend fun getUserInformation(uid: String?): User?
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(name: String, email: String, password: String): Result<Boolean>
    suspend fun signOut()
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService
) : AuthRepository {

    override fun currentUser(): Flow<FirebaseUser?> {
        return authService.currentUser()
    }

    override suspend fun getUserInformation(uid: String?): User? {
        return try {
            withContext(Dispatchers.IO) {
                val user = authService.getUserInformation(uid)
                Log.i("Firestore", "getUserInformation: ${user?.name}")
                user
            }
        } catch (e: Exception) {
            Log.i("Firestore", "getUserInformation: $e")
            null
        }
    }

    override fun hasUser(): Boolean {
        return authService.hasUser()
    }

    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        val result = authService.signIn(email, password)
        return result
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<Boolean> {
        val result = authService.signUp(name, email, password)
        return result
    }

    override suspend fun signOut() {
        authService.signOut()
    }
}