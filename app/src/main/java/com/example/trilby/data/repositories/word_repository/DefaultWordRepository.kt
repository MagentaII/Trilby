package com.example.trilby.data.repositories.word_repository

import com.example.trilby.data.data_sources.database.dao.WordDao
import com.example.trilby.data.data_sources.database.model.toExternalModel
import com.example.trilby.data.data_sources.firebase.UserFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.WordFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.model.toExternalModel
import com.example.trilby.data.data_sources.network.WordNetworkDataSource
import com.example.trilby.data.data_sources.network.model.toExternalModel
import com.example.trilby.data.repositories.word_repository.model.WordForUi
import com.example.trilby.data.repositories.word_repository.model.toEntity
import com.example.trilby.data.repositories.word_repository.model.toFirebaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class DefaultWordRepository @Inject constructor(
    private val wordNetworkDataSource: WordNetworkDataSource,
    private val wordDao: WordDao,
    private val wordFirebaseDataSource: WordFirebaseDataSource,
    private val userFirebaseDataSource: UserFirebaseDataSource,
) : WordRepository {
    private val _cachedWords = MutableStateFlow<List<WordForUi>>(emptyList())
    private val _selectWord = MutableStateFlow(WordForUi.empty)
    private val _tempUserUid = "TEST_o7qzW3pOGGUMVTzmIarBqIaQLDZ2"
    override suspend fun searchWords(query: String) {
        try {
            withContext(Dispatchers.IO) {
                val response = wordNetworkDataSource.getWordList(query = query).toExternalModel()
                _cachedWords.value = response
            }
        } catch (e: Exception) {
            Timber.e("搜尋單字失敗: $e")
            _cachedWords.value = emptyList()
        }
    }

    override fun getNetworkWords(): Flow<List<WordForUi>> {
        return _cachedWords.asStateFlow()
    }

    override fun getWordById(id: String): WordForUi {
        val word = _cachedWords.value.find { it.wordId == id }
        if (word != null) {
            _selectWord.value = word
            return word
        } else {
            Timber.e("$id, 獲取單字為空")
            return WordForUi.empty
        }
//        return flow {
//            val word = _cachedWords.value.find { it.uid == id }
//            if (word != null) {
//                emit(word)
//            } else {
//                Timber.e("$id, 獲取單字為空")
//                emit(ShowWord.empty)
//            }
//        }.catch { e ->
//            Timber.e("$id, 獲取單字失敗: $e")
//        }
    }


    // for Local
    override suspend fun saveWordToLocal(id: String) {
//        val word = _cachedWords.value.find { it.uid == id }
        val word = _selectWord.value
        if (word != WordForUi.empty) {
            try {
                withContext(Dispatchers.IO) {
                    wordDao.insertWords(word.toEntity())
                    // TODO 這不能放在這裡 (可能要使用UseCase)
                    saveWordToRemote(id)
                }
            } catch (e: Exception) {
                Timber.e("$id, 本地單字儲存失敗: $e")
            }
        } else {
            Timber.e("$id, 本地單字儲存錯誤，單字為空")
        }
    }

    override suspend fun deleteWordFromLocal(id: String) {
//        val word = _cachedWords.value.find { it.uid == id }
        val word = _selectWord.value
        if (word != WordForUi.empty) {
            try {
                withContext(Dispatchers.IO) {
                    wordDao.deleteWords(word.toEntity())
                    // TODO 這不能放在這裡 (可能要使用UseCase)
                    deleteWordFromRemote(id)
                }
            } catch (e: Exception) {
                Timber.e("$id, 本地單字刪除失敗: $e")
            }
        } else {
            Timber.e("$id, 本地單字刪除錯誤，單字為空")
        }
    }

    override suspend fun deleteAllWordsFromLocal() {
        try {
            withContext(Dispatchers.IO) {
                wordDao.deleteAllWords()
            }
        } catch (e: Exception) {
            Timber.e("本地全部單字刪除失敗: $e")
        }
    }

    override suspend fun isWordExistInLocal(id: String): Result<Boolean> {
        return runCatching {
            withContext(Dispatchers.IO) {
                wordDao.isWordExist(id)
            }
        }.onFailure { e ->
            Timber.e("$id, 本地單字存在檢查失敗: $e")
        }
    }

    override fun getAllLocalWords(): Flow<List<WordForUi>> {
        return try {
            wordDao.getAllWords().map { wordEntityList -> wordEntityList.toExternalModel() }
        } catch (e: Exception) {
            Timber.e("獲取本地全部單字失敗: $e")
            flowOf(emptyList())
        }
    }

    override suspend fun getLocalWordById(id: String): WordForUi {
        return try {
            wordDao.getWordById(id).toExternalModel().first()
//            wordDao
//                .getWordById(id)
//                .map { it.toExternal() }
//                .map { it.first() }
        } catch (e: Exception) {
            Timber.e("$id, 獲取本地單字失敗: $e")
            WordForUi.empty
        }
    }


    // for Remote
    override suspend fun saveWordToRemote(id: String) {
//        val word = _cachedWords.value.find { it.uid == id }
        val word = _selectWord.value
        try {
            if (userFirebaseDataSource.hasUser()) {
                if (word != WordForUi.empty) {
                    val uid = userFirebaseDataSource.getCurrentUserUid().firstOrNull()
                    if (uid != null) {
                        withContext(Dispatchers.IO) {
                            wordFirebaseDataSource.insertWord(
                                userId = uid,
                                wordList = word.toFirebaseModel()
                            )
                        }
                    } else {
                        Timber.e("$id, 網路單字儲存失敗，User uid為空")
                    }
                } else {
                    Timber.e("$id, 網路單字儲存錯誤，單字為空")
                }
            } else {
                Timber.e("$id, 網路單字儲存錯誤，沒有User")
                // save test
                if (word != WordForUi.empty) {
                    wordFirebaseDataSource.insertWord(
                        userId = _tempUserUid,
                        wordList = word.toFirebaseModel()
                    )
                } else {
                    Timber.e("$id, 網路單字儲存失敗，User uid為空")
                }
                //--------------------
            }
        } catch (e: Exception) {
            Timber.e("$id, 網路單字儲存失敗: $e")
        }
    }

    override suspend fun deleteWordFromRemote(id: String) {
//        val word = _cachedWords.value.find { it.uid == id }
        val word = _selectWord.value
        try {
            if (userFirebaseDataSource.hasUser()) {
                if (word != WordForUi.empty) {
                    val uid = userFirebaseDataSource.getCurrentUserUid().firstOrNull()
                    if (uid != null) {
                        withContext(Dispatchers.IO) {
                            wordFirebaseDataSource.deleteWord(uid, word.toFirebaseModel())
                        }
                    } else {
                        Timber.e("$id, 網路單字刪除失敗，User uid為空")
                    }
                } else {
                    Timber.e("$id, 網路單字刪除錯誤，單字為空")
                }
            } else {
                Timber.e("$id, 網路單字刪除錯誤，沒有User")
            }
        } catch (e: Exception) {
            Timber.e("$id, 網路單字刪除失敗: $e")
        }
    }

    override suspend fun deleteAllWordsFromRemote() {
        try {
            if (userFirebaseDataSource.hasUser()) {
                val uid = userFirebaseDataSource.getCurrentUserUid().firstOrNull()
                if (uid != null) {
                    withContext(Dispatchers.IO) {
                        wordFirebaseDataSource.deleteAllWords(uid)
                    }
                } else {
                    Timber.e("網路全部單字刪除失敗，User uid為空")
                }
            } else {
                Timber.e("網路單字刪除錯誤，沒有User")
            }
        } catch (e: Exception) {
            Timber.e("網路全部單字刪除失敗: $e")
        }
    }

    override suspend fun isWordExistInRemote(id: String): Result<Boolean> {
//        val word = _cachedWords.value.find { it.uid == id }
        val word = _selectWord.value
        // 確認 User 存在
        if (!userFirebaseDataSource.hasUser()) {
            return Result.failure(Exception("網路單字存在檢查錯誤，沒有User"))
        }

        // 如果 word 為空，則返回錯誤
        if (word == WordForUi.empty) {
            Timber.e("$id, 網路單字查詢錯誤，單字為空")
            return Result.failure(Exception("網路單字查詢錯誤，單字為空"))
        }

        // 確認 UID 存在
        val uid = userFirebaseDataSource.getCurrentUserUid().firstOrNull()
            ?: return Result.failure(Exception("網路單字存在檢查錯誤，User uid為空"))

        // 執行異步操作並返回結果
        return withContext(Dispatchers.IO) {
            // 呼叫 Firebase DataSource 並返回其結果
            wordFirebaseDataSource.isWordExist(uid, word.toFirebaseModel())
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllRemoteWords(): Flow<List<WordForUi>> {
        return try {
            if (userFirebaseDataSource.hasUser()) {
                userFirebaseDataSource.getCurrentUserUid().flatMapLatest { uid ->
                    if (uid != null) {
                        // 這裡進行資料型別的轉換
                        wordFirebaseDataSource.getAllWords(uid).map { fbWords ->
                            fbWords.toExternalModel()
                        }
                    } else {
                        flowOf(emptyList())
                    }
                }
            } else {
                flowOf(emptyList())
            }
        } catch (e: Exception) {
            Timber.e("獲取網路全部單字失敗: $e")
            flowOf(emptyList())
        }
    }

    override suspend fun getRemoteWordById(id: String): WordForUi {
//        val word = _cachedWords.value.find { it.uid == id }
        val word = _selectWord.value
        // 確認 User 存在
        if (!userFirebaseDataSource.hasUser()) {
            return WordForUi.empty
        }

        // 如果 word 為空，則返回錯誤
        if (word == WordForUi.empty) {
            Timber.e("$id, 網路單字查詢錯誤，單字為空")
            return WordForUi.empty
        }

        // 確認 UID 存在
        val uid = userFirebaseDataSource.getCurrentUserUid().firstOrNull()
            ?: return WordForUi.empty

        // 執行異步操作並返回結果
        return wordFirebaseDataSource.getWord(uid, word.toFirebaseModel()).toExternalModel().first()
    }

}