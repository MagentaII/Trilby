package com.example.trilby.data.repositories.auth_repository

import android.util.Log
import com.example.trilby.data.data_sources.database.dao.WordDao
import com.example.trilby.data.data_sources.datastore.UserPreferences
import com.example.trilby.data.data_sources.firebase.UserFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.WordFirebaseDataSource
import com.example.trilby.data.repositories.auth_repository.model.User
import com.example.trilby.data.repositories.auth_repository.util.toExternal
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
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

    override fun getUserInformation(uid: String?): Flow<User?> {
        return flow {
            val userInfo = userFirebaseDataSource.getUserInformation(uid)
            if (userInfo != null) {
                emit(userInfo.toExternal())
            } else {
                Timber.e("$uid, 取得使用者資訊錯誤: userInfo is null")
                emit(null)
            }
        }.catch { e ->
            Timber.e("$uid, 取得使用者資訊錯誤: $e")
        }.flowOn(Dispatchers.IO)
    }

    override fun hasUser(): Boolean {
        return userFirebaseDataSource.hasUser()
    }

    override fun signIn(email: String, password: String): Flow<Boolean> {
        return flow {
            userFirebaseDataSource.signIn(email, password)
        }.catch { e ->
            Timber.e("登入錯誤: $e")
        }
        val result = userFirebaseDataSource.signIn(email, password)
        return result
    }

    override fun signUp(name: String, email: String, password: String): Flow<Boolean> {
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