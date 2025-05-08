package com.example.trilby.data.repositories.auth_repository

import android.util.Log
import com.example.trilby.data.data_sources.datastore.UserPreferences
import com.example.trilby.data.data_sources.firebase.UserFirebaseDataSource
import com.example.trilby.data.repositories.auth_repository.model.User
import com.example.trilby.data.repositories.auth_repository.util.toExternal
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultAuthRepository @Inject constructor(
    private val userFirebaseDataSource: UserFirebaseDataSource,
    private val userPreferences: UserPreferences
) : AuthRepository {

    override fun currentUser(): Flow<FirebaseUser?> {
        return userFirebaseDataSource.currentUser()
    }

    override fun getCurrentUserUid(): Flow<String?> {
        return userFirebaseDataSource.getCurrentUserUid()
    }

    override suspend fun getUserInformation(uid: String?): User? {
        return try {
            withContext(Dispatchers.IO) {
                val user = userFirebaseDataSource.getUserInformation(uid)
                Log.i("Firestore", "getUserInformation: ${user?.name}")
                user?.toExternal()
            }
        } catch (e: Exception) {
            Log.i("Firestore", "getUserInformation: $e")
            null
        }
    }

    override fun hasUser(): Boolean {
        return userFirebaseDataSource.hasUser()
    }

    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        val result = userFirebaseDataSource.signIn(email, password)
        return result
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<Boolean> {
        val result = userFirebaseDataSource.signUp(name, email, password)
        return result
    }

    override suspend fun signOut() {
        userFirebaseDataSource.signOut()
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