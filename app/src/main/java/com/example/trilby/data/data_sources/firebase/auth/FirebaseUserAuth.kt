package com.example.trilby.data.data_sources.firebase.auth

import com.example.trilby.data.data_sources.firebase.UserFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.model.FbUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseUserAuth @Inject constructor(
    private val auth: FirebaseAuth,
    private val firebase: Firebase,
) : UserFirebaseDataSource {

    private val _currentUserFlow = MutableStateFlow(auth.currentUser)
//    val currentUserFlow: StateFlow<FirebaseUser?> = _currentUserFlow.asStateFlow()

    init {
        auth.addAuthStateListener { firebaseAuth ->
            _currentUserFlow.value = firebaseAuth.currentUser
        }
    }

    override fun currentUser(): Flow<FirebaseUser?> {
        return _currentUserFlow
    }

    override fun getCurrentUserUid(): Flow<String?> {
        return currentUser().map { user ->
            user?.uid
        }
    }

    override suspend fun getUserInformation(uid: String?): FbUser? {
        val db = firebase.firestore
        return try {
            if (uid != null) {
                val document = db.collection("users").document(uid).get().await()
                if (document.exists()) {
                    document.toObject<FbUser>()
                } else {
                    Timber.e("Not found user information")
                    null
                }
            } else {
                Timber.e("User uid is null")
                null
            }
        } catch (e: Exception) {
            Timber.e("Error while getting user information: $e")
            null
        }
    }

    override fun hasUser(): Boolean {
        return auth.currentUser != null
    }

    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.uid != null
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): Boolean {
        val db = firebase.firestore
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid

            if (userId != null) {
                val user = mapOf(
                    "name" to name,
                    "email" to email,
                    "createdAt" to FieldValue.serverTimestamp()
                )

                db.collection("users").document(userId).set(user).await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signOut() {
        try {
            auth.signOut()
        } catch (e: Exception) {
            Timber.e("Sign out error: $e")
        }
    }
}

