package com.example.trilby.data.repositories.word_repository

import com.example.trilby.data.repositories.word_repository.model.WordForUi
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
    fun getNetworkWords(): Flow<List<WordForUi>> // 會需要隨著搜尋結果更新
    fun getWordById(id: String): WordForUi

    // for Local
    suspend fun saveWordToLocal(id: String)
    suspend fun deleteWordFromLocal(id: String)
    suspend fun deleteAllWordsFromLocal()
    suspend fun isWordExistInLocal(id: String): Result<Boolean>
    fun getAllLocalWords(): Flow<List<WordForUi>>
    suspend fun getLocalWordById(id: String): WordForUi

    // for Remote
    suspend fun saveWordToRemote(id: String)
    suspend fun deleteWordFromRemote(id: String)
    suspend fun deleteAllWordsFromRemote()
    suspend fun isWordExistInRemote(id: String): Result<Boolean>
    fun getAllRemoteWords(): Flow<List<WordForUi>>
    suspend fun getRemoteWordById(id: String): WordForUi
}