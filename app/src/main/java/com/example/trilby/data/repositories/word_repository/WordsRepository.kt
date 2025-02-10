package com.example.trilby.data.repositories.word_repository

import android.util.Log
import com.example.trilby.BuildConfig
import com.example.trilby.data.sources.local.WordDao
import com.example.trilby.data.sources.network.word_api_network_source.DictionaryApiService
import com.example.trilby.data.sources.network.word_firestore_network_source.WordsFirestoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface WordRepository {
    // Network
    var searchWords: List<ShowWord>
    suspend fun search(query: String): List<ShowWord>

    // Local
    suspend fun saveWordToLocal(showWord: ShowWord)
    suspend fun saveAllWordsToLocal(showWords: List<ShowWord>)
    suspend fun fetchAllWordsToLocal(userUid: String?): List<ShowWord>
    suspend fun deleteWordForLocal(word: ShowWord)
    suspend fun isWordExistInLocal(word: ShowWord): Boolean
    suspend fun deleteAllWordsForLocal()
    suspend fun haveWordsInLocal(): Boolean

    // firestore
    suspend fun fetchAllWordFromFirestore(userUid: String?): List<ShowWord>
    suspend fun saveWordToFirestore(showWord: ShowWord, userUid: String?)
    suspend fun deleteWordForFirestore(showWord: ShowWord, userUid: String?)
}

class WordRepositoryImpl @Inject constructor(
    private val dictionaryApiService: DictionaryApiService,
    private val wordDao: WordDao,
    private val wordsFirestoreService: WordsFirestoreService,
) : WordRepository {

    // Network
    override var searchWords: List<ShowWord> = emptyList()
    override suspend fun search(query: String): List<ShowWord> {
        val networkWords = try {
            Log.i("TAG", "search: ")
            withContext(Dispatchers.IO) {
                Log.i("TAG", "search, api key: ${BuildConfig.API_KEY}")
                dictionaryApiService.searchWords(word = query)
            }
        } catch (e: Exception) {
            Log.d("TAG", "search: $e")
            emptyList()
        }
        networkWords.forEachIndexed { index, networkWord ->
            Log.i("TAG", "search, definitions${index}: ${networkWord.def}")
            networkWord.def.forEach { de ->
                Log.i("TAG", "search, definition, de.sseq: ${de.sseq}: ")
            }
        }
        searchWords = networkWords.toExternal()
            .groupBy { word -> word.wordId.substringBefore(":") }
            .map { (uid, words) ->
                ShowWord(
                    uid = uid,
                    words = words
                )
            }
        return networkWords.toExternal()
            .groupBy { word -> word.wordId.substringBefore(":") }
            .map { (uid, words) ->
                ShowWord(
                    uid = uid,
                    words = words
                )
            }
    }

    // Local
    override suspend fun saveWordToLocal(showWord: ShowWord) {
        val words: List<Word> = List(showWord.words.size) { index ->
            Word(
                wordId = showWord.words[index].wordId,
                wordUuid = showWord.words[index].wordUuid,
                headword = showWord.words[index].headword,
                wordPrs = showWord.words[index].wordPrs,
                label = showWord.words[index].label,
                shortDef = showWord.words[index].shortDef,
            )
        }
        try {
            withContext(Dispatchers.IO) {
                wordDao.insertWords(words = words.toLocal())
            }
        } catch (e: Exception) {
            Log.d("TAG", "saveWord: $e")
        }
    }

    override suspend fun saveAllWordsToLocal(showWords: List<ShowWord>) {
        showWords.forEach { showWord ->
            val words: List<Word> = List(showWord.words.size) { index ->
                Word(
                    wordId = showWord.words[index].wordId,
                    wordUuid = showWord.words[index].wordUuid,
                    headword = showWord.words[index].headword,
                    wordPrs = showWord.words[index].wordPrs,
                    label = showWord.words[index].label,
                    shortDef = showWord.words[index].shortDef,
                )
            }
            try {
                withContext(Dispatchers.IO) {
                    wordDao.insertWords(words = words.toLocal())
                    Log.i("Room", "saveAllWords: Success save word")
                }
            } catch (e: Exception) {
                Log.i("Room", "saveAllWords: Failure save word, and $e")
            }

        }
    }

    override suspend fun fetchAllWordsToLocal(userUid: String?): List<ShowWord> {
        val haveWordsFromLocal = haveWordsInLocal()
        if (!haveWordsFromLocal) {
            try {
                if (!userUid.isNullOrEmpty()) {
                    withContext(Dispatchers.IO) {
                        val showWords = fetchAllWordFromFirestore(userUid)
                        saveAllWordsToLocal(showWords)
                    }
                } else {
                    Log.i("Room", "fetchAllWordsToLocal: userUid is empty")
                }
            } catch (e: Exception) {
                Log.d("TAG", "getAllWords: $e")
            }
        }
        val localWords = try {
            withContext(Dispatchers.IO) {
                wordDao.getAllWords()
            }
        } catch (e: Exception) {
            Log.d("TAG", "getAllWords: $e")
            emptyList()
        }
        val showWords = localWords.toExternal()
            .groupBy { word -> word.wordId.substringBefore(":") }
            .map { (uid, words) ->
                ShowWord(
                    uid = uid,
                    words = words
                )
            }
        return showWords
    }

    override suspend fun deleteWordForLocal(word: ShowWord) {
        val words: List<Word> = List(word.words.size) { index ->
            Word(
                wordId = word.words[index].wordId,
                wordUuid = word.words[index].wordUuid,
                headword = word.words[index].headword,
                wordPrs = word.words[index].wordPrs,
                label = word.words[index].label,
                shortDef = word.words[index].shortDef,
            )
        }
        try {
            withContext(Dispatchers.IO) {
                wordDao.deleteWords(words = words.toLocal())
            }
        } catch (e: Exception) {
            Log.d("TAG", "deleteWord: $e")
        }
    }

    override suspend fun isWordExistInLocal(word: ShowWord): Boolean {
        val words: List<Word> = List(word.words.size) { index ->
            Word(
                wordId = word.words[index].wordId,
                wordUuid = word.words[index].wordUuid,
                headword = word.words[index].headword,
                wordPrs = word.words[index].wordPrs,
                label = word.words[index].label,
                shortDef = word.words[index].shortDef,
            )
        }
        val isExist = try {
            withContext(Dispatchers.IO) {
                wordDao.isWordExist(id = words[0].toLocal().id)
            }
        } catch (e: Exception) {
            Log.d("TAG", "isWordExist: $e")
            false
        }
        return isExist
    }

    override suspend fun deleteAllWordsForLocal() {
        try {
            withContext(Dispatchers.IO) {
                wordDao.deleteAllWords()
                Log.i("Room", "deleteAllWords: Success delete all words")
            }
        } catch (e: Exception) {
            Log.i("Room", "deleteAllWords: Failure delete all words, and $e")
        }
    }

    override suspend fun haveWordsInLocal(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val hasWords = wordDao.hasWords()
                Log.i("Room", "hasWords: Success $hasWords")
                hasWords
            }
        } catch (e: Exception) {
            Log.i("Room", "hasWords: Failure $e")
            false
        }
    }

    override suspend fun fetchAllWordFromFirestore(userUid: String?): List<ShowWord> {
        return try {
            withContext(Dispatchers.IO) {
                if (!userUid.isNullOrEmpty()) {
                    wordsFirestoreService.fetchAllWordFromFirestore(userUid = userUid).toExternal()
                        .groupBy { word -> word.wordId.substringBefore(":") }
                        .map { (uid, words) ->
                            ShowWord(
                                uid = uid,
                                words = words
                            )
                        }
                } else {
                    Log.i("repository", "fetchAllWordFromFirestore: userUid is empty")
                    emptyList()
                }
            }
        } catch (e: Exception) {
            Log.d("repository", "getFirestoreWord: $e")
            emptyList()
        }
    }

    override suspend fun saveWordToFirestore(showWord: ShowWord, userUid: String?) {
        val firestoreWords = showWord.words.map { word ->
            word.toFirestore()
        }
        try {
            withContext(Dispatchers.IO) {
                if (!userUid.isNullOrEmpty()) {
                    wordsFirestoreService.saveWordToFirestore(firestoreWords, userUid)
                } else {
                    Log.i("repository", "addFirestoreWords: userUid is empty")
                }
            }
        } catch (e: Exception) {
            Log.d("repository", "addFirestoreWords: $e")
        }

    }

    override suspend fun deleteWordForFirestore(showWord: ShowWord, userUid: String?) {
        val firestoreWords = showWord.words.map { word ->
            word.toFirestore()
        }
        try {
            withContext(Dispatchers.IO) {
                if (!userUid.isNullOrEmpty()) {
                    wordsFirestoreService.deleteWordForFirestore(firestoreWords, userUid)
                } else {
                    Log.i("repository", "deleteFirestoreWords: userUid is empty")
                }
            }
        } catch (e: Exception) {
            Log.d("repository", "deleteFirestoreWords: $e")
        }
    }
}