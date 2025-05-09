package com.example.trilby.data.repositories.word_repository

import com.example.trilby.data.data_sources.database.dao.WordDao
import com.example.trilby.data.data_sources.firebase.UserFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.WordFirebaseDataSource
import com.example.trilby.data.data_sources.network.WordNetworkDataSource
import com.example.trilby.data.repositories.word_repository.model.ShowWord
import com.example.trilby.data.repositories.word_repository.util.toExternal
import com.example.trilby.data.repositories.word_repository.util.toFirestore
import com.example.trilby.data.repositories.word_repository.util.toLocal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultWordRepository @Inject constructor(
    private val wordNetworkDataSource: WordNetworkDataSource,
    private val wordDao: WordDao,
    private val wordFirebaseDataSource: WordFirebaseDataSource,
    private val userFirebaseDataSource: UserFirebaseDataSource,
) : WordRepository {
    private val _cachedWords = MutableStateFlow<List<ShowWord>>(emptyList())
    override suspend fun searchWords(query: String) {
        try {
            withContext(Dispatchers.IO) {
                val response = wordNetworkDataSource.getWordList(query = query).toExternal()
                _cachedWords.value = response
            }
        } catch (e: Exception) {
            Timber.e("搜尋單字失敗: $e")
            _cachedWords.value = emptyList()
        }
    }

    override fun getWords(): Flow<List<ShowWord>> {
        return _cachedWords.asStateFlow()
    }

    override fun getWordById(id: String): Flow<ShowWord> {
        return flow {
            val word = _cachedWords.value.find { it.uid == id }
            if (word != null) {
                emit(word)
            } else {
                Timber.e("$id, 獲取單字為空")
                emit(ShowWord.empty)
            }
        }.catch { e ->
            Timber.e("$id, 獲取單字失敗: $e")
        }
    }

    override suspend fun saveWord(id: String) {
        val word = _cachedWords.value.find { it.uid == id }
        if (userFirebaseDataSource.hasUser()) {
            if (word != null) {
                userFirebaseDataSource.getCurrentUserUid().collect { uid ->
                    if (uid != null) {
                        wordFirebaseDataSource.insertWord(word.toFirestore(), uid)
                        wordDao.insertWords(word.toLocal())
                    } else {
                        Timber.e("$id, 網路單字儲存失敗，User uid為空")
                    }
                }
            } else {
                Timber.e("$id, 網路單字儲存錯誤，單字為空")
            }
        } else {
            if (word != null) {
                wordDao.insertWords(word.toLocal())
            } else {
                Timber.e("$id, 本地單字儲存錯誤，單字為空")
            }
        }
    }

    override suspend fun deleteWord(id: String) {
        val word = _cachedWords.value.find { it.uid == id }
        if (userFirebaseDataSource.hasUser()) {
            if (word != null) {
                userFirebaseDataSource.getCurrentUserUid().collect { uid ->
                    if (uid != null) {
                        wordFirebaseDataSource.deleteWord(word.toFirestore(), uid)
                        wordDao.deleteWords(word.toLocal())
                    } else {
                        Timber.e("$id, 網路單字刪除失敗，User uid為空")
                    }
                }
            } else {
                Timber.e("$id, 網路單字刪除錯誤，單字為空")
            }
        } else {
            if (word != null) {
                wordDao.deleteWords(word.toLocal())
            } else {
                Timber.e("$id, 本地單字刪除錯誤，單字為空")
            }
        }
    }

    override suspend fun deleteAllWords() {
        if (userFirebaseDataSource.hasUser()) {
            userFirebaseDataSource.getCurrentUserUid().collect { uid ->
                if (uid != null) {
                    wordFirebaseDataSource.getAllWords(uid)
                    wordDao.deleteAllWords()
                } else {
                    Timber.e("網路全部單字刪除失敗，User uid為空")
                }
            }
        } else {
            Timber.i("刪除本地全部單字")
            wordDao.deleteAllWords()
        }
    }

    override suspend fun isWordExist(id: String): Boolean {
        return wordDao.isWordExist(id)
    }

}