package com.example.trilby.data.repositories

import android.util.Log
import com.example.trilby.data.sources.network.DictionaryApiService
import com.example.trilby.data.sources.network.NetworkWords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface WordRepository {
    suspend fun search(query: String): List<NetworkWords>
}

class WordRepositoryImpl @Inject constructor(
    private val dictionaryApiService: DictionaryApiService,
): WordRepository {

    override suspend fun search(query: String): List<NetworkWords> {
        val words = try {
            Log.i("TAG", "search: ")
            withContext(Dispatchers.IO) {
                dictionaryApiService.searchWords(word = query)
            }
        } catch (e: Exception) {
            Log.e("TAG", "search: $e", e)
            emptyList()
        }
        Log.i("TAG", "search, words: $words")
        return words
    }
}