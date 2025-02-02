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
    suspend fun saveWord(word: ShowWord)
    suspend fun getAllWords(): List<ShowWord>
    suspend fun deleteWord(word: ShowWord)
    suspend fun isWordExist(word: ShowWord): Boolean

    // firestore
    suspend fun getFirestoreWords(): List<ShowWord>
    suspend fun addFirestoreWords(showWord: ShowWord)
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
    override suspend fun saveWord(word: ShowWord) {
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
                wordDao.insertWords(words = words.toLocal())
            }
        } catch (e: Exception) {
            Log.d("TAG", "saveWord: $e")
        }
    }

    override suspend fun getAllWords(): List<ShowWord> {
        val localWords = try {
            withContext(Dispatchers.IO) {
                wordDao.getAllWords()
            }
        } catch (e: Exception) {
            Log.d("TAG", "getAllWords: $e")
            emptyList()
        }
        return localWords.toExternal()
            .groupBy { word -> word.wordId.substringBefore(":") }
            .map { (uid, words) ->
                ShowWord(
                    uid = uid,
                    words = words
                )
            }
    }

    override suspend fun deleteWord(word: ShowWord) {
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

    override suspend fun isWordExist(word: ShowWord): Boolean {
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

    override suspend fun getFirestoreWords(): List<ShowWord> {
        return try {
            withContext(Dispatchers.IO) {
                wordsFirestoreService.getWord().toExternal()
                    .groupBy { word -> word.wordId.substringBefore(":") }
                    .map { (uid, words) ->
                        ShowWord(
                            uid = uid,
                            words = words
                        )
                    }
            }
        } catch (e: Exception) {
            Log.d("repository", "getFirestoreWord: $e")
            emptyList()
        }
    }

    override suspend fun addFirestoreWords(showWord: ShowWord) {
        val firestoreWords = showWord.words.map { word ->
            word.toFirestore()
        }
        try {
            withContext(Dispatchers.IO) {
                wordsFirestoreService.addWord(firestoreWords)
            }
        } catch (e: Exception) {
            Log.d("repository", "addFirestoreWords: $e")
        }

    }
}