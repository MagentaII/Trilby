package com.example.trilby.data.repositories

import android.util.Log
import com.example.trilby.data.sources.local.WordDao
import com.example.trilby.data.sources.network.DictionaryApiService
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
}

class WordRepositoryImpl @Inject constructor(
    private val dictionaryApiService: DictionaryApiService,
    private val wordDao: WordDao,
) : WordRepository {

    // Network
    override var searchWords: List<ShowWord> = emptyList()
    override suspend fun search(query: String): List<ShowWord> {
        val networkWords = try {
            Log.i("TAG", "search: ")
            withContext(Dispatchers.IO) {
                dictionaryApiService.searchWords(word = query)
            }
        } catch (e: Exception) {
            Log.d("TAG", "search: $e")
            emptyList()
        }
        Log.i("TAG", "search, words: $networkWords")
        searchWords = networkWords.toExternal()
            .groupBy { word -> word.wordId.substringBefore(":") }
            .map { (uid, words) ->
                ShowWord(
                    uid = uid,
                    wordId = words.map { word -> word.wordId },
                    headword = words.map { word -> word.headword },
                    label = words.map { word -> word.label },
                    definition = words.map { word -> word.definition },
                )
            }
        return networkWords.toExternal()
            .groupBy { word -> word.wordId.substringBefore(":") }
            .map { (uid, words) ->
                ShowWord(
                    uid = uid,
                    wordId = words.map { word -> word.wordId },
                    headword = words.map { word -> word.headword },
                    label = words.map { word -> word.label },
                    definition = words.map { word -> word.definition },
                )
            }
//        return networkWords.toExternal()
//        return searchWords
    }

    // Local
    override suspend fun saveWord(word: ShowWord) {
        val words: List<Word> = List(word.wordId.size) { index ->
            Word(
                uid = word.uid,
                wordId = word.wordId[index],
                headword = word.headword[index],
                label = word.label[index],
                definition = word.definition[index],
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
//        favoriteWords = localWords.toExternal()
        return localWords.toExternal()
            .groupBy { word -> word.wordId.substringBefore(":") }
            .map { (uid, words) ->
                ShowWord(
                    uid = uid,
                    wordId = words.map { word -> word.wordId },
                    headword = words.map { word -> word.headword },
                    label = words.map { word -> word.label },
                    definition = words.map { word -> word.definition },
                )
            }
//        return words
    }

    override suspend fun deleteWord(word: ShowWord) {
        val words: List<Word> = List(word.wordId.size) { index ->
            Word(
                uid = word.uid,
                wordId = word.wordId[index],
                headword = word.headword[index],
                label = word.label[index],
                definition = word.definition[index],
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
        val words: List<Word> = List(word.wordId.size) { index ->
            Word(
                uid = word.uid,
                wordId = word.wordId[index],
                headword = word.headword[index],
                label = word.label[index],
                definition = word.definition[index],
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
}