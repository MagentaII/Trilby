package com.example.trilby.data.repositories

import android.util.Log
import com.example.trilby.data.sources.local.WordDao
import com.example.trilby.data.sources.network.DictionaryApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface WordRepository {
    // Network
    var searchWords: List<Word>
    suspend fun search(query: String): List<Word>

    // Local
    suspend fun saveWord(word: Word)
    suspend fun getAllWords(): List<Word>
    suspend fun deleteWord(word: Word)
    suspend fun isWordExist(word: Word): Boolean
}

class WordRepositoryImpl @Inject constructor(
    private val dictionaryApiService: DictionaryApiService,
    private val wordDao: WordDao,
) : WordRepository {

    // Network
    override var searchWords: List<Word> = emptyList()
    override suspend fun search(query: String): List<Word> {
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
        return networkWords.toExternal()
//        return searchWords
    }

    // Local
    override suspend fun saveWord(word: Word) {
        try {
            withContext(Dispatchers.IO) {
                wordDao.insertWord(word = word.toLocal())
            }
        } catch (e: Exception) {
            Log.d("TAG", "saveWord: $e")
        }
    }

    override suspend fun getAllWords(): List<Word> {
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
//        return words
    }

    override suspend fun deleteWord(word: Word) {
        try {
            withContext(Dispatchers.IO) {
                wordDao.deleteWord(word = word.toLocal())
            }
        } catch (e: Exception) {
            Log.d("TAG", "deleteWord: $e")
        }
    }

    override suspend fun isWordExist(word: Word): Boolean {
        val isExist = try {
            withContext(Dispatchers.IO) {
                wordDao.isWordExist(id = word.toLocal().id)
            }
        } catch (e: Exception) {
            Log.d("TAG", "isWordExist: $e")
            false
        }
        return isExist
    }
}