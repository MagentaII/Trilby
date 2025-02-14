package com.example.trilby.data.sources.network.auth_network_source

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


interface AuthService {
    fun currentUser(): Flow<FirebaseUser?>
    fun getCurrentUserUid(): Flow<String?>
    suspend fun getUserInformation(uid: String?): NetworkUser?
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(name: String, email: String, password: String): Result<Boolean>
    suspend fun signOut()
}

class AuthServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebase: Firebase,
) : AuthService {

    private val _currentUserFlow = MutableStateFlow(auth.currentUser)
    val currentUserFlow: StateFlow<FirebaseUser?> = _currentUserFlow.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUserFlow.value = firebaseAuth.currentUser
        }
    }

    override fun currentUser(): Flow<FirebaseUser?> {
        return currentUserFlow
    }

    override fun getCurrentUserUid(): Flow<String?> {
        return currentUser().map { user ->
            user?.uid
        }
    }

    override suspend fun getUserInformation(uid: String?): NetworkUser? {
        val db = firebase.firestore
        return try {
            if (uid != null) {
                Log.i("Firestore", "getUserInformation: $uid")
                val document = db.collection("users").document(uid).get().await()
                if (document.exists()) {
                    Log.i("Firestore", "getUserInformation: ${document.exists()}")
                    document.toObject<NetworkUser>()
                } else {
                    Log.i("Firestore", "getUserInformation: document no exists ")
                    null
                }
            } else {
                Log.i("Firestore", "getUserInformation: UID is empty")
                null
            }
        } catch (e: Exception) {
            Log.e("Firestore", "獲取用戶資訊失敗: ${e.message}")
            null // 發生錯誤時回傳 `null`
        }
    }

    override fun hasUser(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String): Result<Boolean> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid

            if (userId != null) {
                Result.success(true)
            } else {
                Result.failure(Exception("User ID is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<Boolean> {
        val db = firebase.firestore
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid

            if (userId != null) {
                val user = hashMapOf(
                    "name" to name,
                    "email" to email,
                    "createdAt" to FieldValue.serverTimestamp()
                )

                db.collection("users").document(userId).set(user).await()
                Result.success(true)
            } else {
                Result.failure(Exception("User ID is null"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }
}