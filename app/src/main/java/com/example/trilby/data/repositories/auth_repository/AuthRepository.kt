package com.example.trilby.data.repositories.auth_repository

import android.util.Log
import com.example.trilby.data.data_sources.datastore.UserPreferences
import com.example.trilby.data.data_sources.firebase.UserFirebaseDataSource
import com.example.trilby.data.repositories.auth_repository.model.User
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