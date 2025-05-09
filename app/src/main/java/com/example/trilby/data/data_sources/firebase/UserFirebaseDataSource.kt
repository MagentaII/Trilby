package com.example.trilby.data.data_sources.firebase

import com.example.trilby.data.data_sources.firebase.model.FbUser
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserFirebaseDataSource {
    fun currentUser(): Flow<FirebaseUser?>
    fun getCurrentUserUid(): Flow<String?>
    suspend fun getUserInformation(uid: String?): FbUser?
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(name: String, email: String, password: String): Boolean
    suspend fun signOut()
}