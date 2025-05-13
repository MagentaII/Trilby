package com.example.trilby.data.repositories.auth_repository

import com.example.trilby.data.data_sources.datastore.UserPreferences
import com.example.trilby.data.data_sources.firebase.UserFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.model.toExternalModel
import com.example.trilby.data.repositories.auth_repository.model.User
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
                emit(userInfo.toExternalModel())
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

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                userFirebaseDataSource.signIn(email, password)
            }
        } catch (e: Exception) {
            Timber.e("登入失敗: $e")
            false
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                userFirebaseDataSource.signUp(name, email, password)
            }
        } catch (e: Exception) {
            Timber.e("註冊失敗: $e")
            false
        }
    }

    override suspend fun signOut() {
        try {
            withContext(Dispatchers.IO) {
                userFirebaseDataSource.signOut()
            }
        } catch (e: Exception) {
            Timber.e("Sign out error: $e")
        }

    }

//    override suspend fun getUserUid(): String? {
//        return withContext(Dispatchers.IO) {
//            userPreferences.getUserUid()
//        }
//    }
//
//    override suspend fun saveUserUid(userUid: String?) {
//        withContext(Dispatchers.IO) {
//            userPreferences.saveUserUid(userUid)
//        }
//    }
}