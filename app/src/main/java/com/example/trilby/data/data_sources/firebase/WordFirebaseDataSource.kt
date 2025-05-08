package com.example.trilby.data.data_sources.firebase

import com.example.trilby.data.data_sources.firebase.model.FirestoreWord

interface WordFirebaseDataSource {
    suspend fun saveWordToFirestore(words: List<FirestoreWord>, userUid: String?)
    suspend fun fetchAllWordFromFirestore(userUid: String?): List<FirestoreWord>
    suspend fun deleteWordForFirestore(words: List<FirestoreWord>, userUid: String?)
}
