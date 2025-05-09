package com.example.trilby.data.repositories.word_repository

import com.example.trilby.data.repositories.word_repository.model.ShowWord
import kotlinx.coroutines.flow.Flow

//interface WordRepository {
//    // Network
//    var words: List<ShowWord>
//    fun searchWords(query: String): Flow<List<ShowWord>>
//    fun getWordById(wordId: String): Flow<ShowWord>;
//
//    // Local
//    suspend fun saveWordToLocal(showWord: ShowWord)
//    suspend fun saveAllWordsToLocal(showWords: List<ShowWord>)
//    fun fetchAllWordsToLocal(userUid: String?): Flow<List<ShowWord>>
//    suspend fun deleteWordForLocal(word: ShowWord)
//    suspend fun isWordExistInLocal(word: ShowWord): Boolean
//    suspend fun deleteAllWordsForLocal()
//    suspend fun haveWordsInLocal(): Boolean
//
//    // firestore
//    suspend fun fetchAllWordFromFirestore(userUid: String?): List<ShowWord>
//    suspend fun saveWordToFirestore(showWord: ShowWord, userUid: String?)
//    suspend fun deleteWordForFirestore(showWord: ShowWord, userUid: String?)
//}

interface WordRepository {
    suspend fun searchWords(query: String)
    fun getWords(): Flow<List<ShowWord>>
    fun getWordById(id: String): Flow<ShowWord>

    // for database
    suspend fun saveWord(id: String)
    suspend fun deleteWord(id: String)
    suspend fun deleteAllWords()
    suspend fun isWordExist(id: String): Boolean
}