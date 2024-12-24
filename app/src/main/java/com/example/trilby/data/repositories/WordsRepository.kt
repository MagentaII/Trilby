package com.example.trilby.data.repositories

import android.util.Log
import com.example.trilby.data.sources.network.NetworkDataSource
import com.example.trilby.data.sources.network.NetworkWords
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface WordRepository {
    suspend fun search(query: String): List<NetworkWords>
}

class WordRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
): WordRepository {

    override suspend fun search(query: String): List<NetworkWords> {
        return withContext(Dispatchers.IO) {
            networkDataSource.fetchWords(query = query)
        }
    }


}