package com.example.trilby.data.repositories.auth_repository

import android.util.Log
import com.example.trilby.data.sources.local.UserPreferences
import com.example.trilby.data.sources.network.auth_network_source.AuthService
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {

    // Firebase
    fun currentUser(): Flow<FirebaseUser?>
    fun getCurrentUserUid(): Flow<String?>
    suspend fun getUserInformation(uid: String?): User?
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(name: String, email: String, password: String): Result<Boolean>
    suspend fun signOut()

    // DataStore
    suspend fun getUserUid(): String?
    suspend fun saveUserUid(userUid: String?)
}

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override fun currentUser(): Flow<FirebaseUser?> {
        return authService.currentUser()
    }

    override fun getCurrentUserUid(): Flow<String?> {
        return authService.getCurrentUserUid()
    }

    override suspend fun getUserInformation(uid: String?): User? {
        return try {
            withContext(Dispatchers.IO) {
                val user = authService.getUserInformation(uid)
                Log.i("Firestore", "getUserInformation: ${user?.name}")
                user?.toExternal()
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

    override suspend fun getUserUid(): String? {
        return withContext(Dispatchers.IO) {
            userPreferences.getUserUid()
        }
    }

    override suspend fun saveUserUid(userUid: String?) {
        withContext(Dispatchers.IO) {
            userPreferences.saveUserUid(userUid)
        }
    }
}