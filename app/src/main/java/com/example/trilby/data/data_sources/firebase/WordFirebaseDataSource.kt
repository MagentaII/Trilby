package com.example.trilby.data.data_sources.firebase

import com.example.trilby.data.data_sources.firebase.model.FbWord
import kotlinx.coroutines.flow.Flow

//interface WordFirebaseDataSource {
//    suspend fun saveWordToFirestore(words: List<FirestoreWord>, userUid: String?)
//    suspend fun fetchAllWordFromFirestore(userUid: String?): List<FirestoreWord>
//    suspend fun deleteWordForFirestore(words: List<FirestoreWord>, userUid: String?)
//}

/**
 * Firestore structure
 *
 * users/{userId}/words/{wordId}
 *
 * {
 *   "wordId": "book:2",
 *   "addedAt": "2025-05-14T10:00:00Z",
 *   "hasReview": false
 * }
 *
 * dictionary/{wordId}
 *
 * {
 *   "baseWord": "book",
 *   "pos": "noun",
 *   "definition": "a set of printed pages...",
 *   "example": "She read a book.",
 *   "createdBy": "admin" // 或 crowdsource 機制
 * }
 */

interface WordFirebaseDataSource {
    suspend fun insertWord(userId: String, wordList: List<FbWord>)
    fun getAllWords(userId: String): Flow<List<FbWord>>
    suspend fun deleteWord(userId: String, wordList: List<FbWord>)
    suspend fun deleteAllWords(userId: String)
    suspend fun isWordExist(userId: String, wordList: List<FbWord>): Result<Boolean>
    suspend fun getWord(userId: String, wordList: List<FbWord>): List<FbWord>
}