package com.example.trilby.data.repositories.word_repository

import com.example.trilby.data.data_sources.network.WordNetworkDataSource
import com.example.trilby.data.repositories.word_repository.model.ShowWord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NetworkWordRepository @Inject constructor(
    private val wordNetworkDataSource: WordNetworkDataSource,
): WordRepository {
    override fun getWords(): Flow<List<ShowWord>> {
        TODO("Not yet implemented")
    }

    override fun getWordById(id: String): Flow<ShowWord> {
        TODO("Not yet implemented")
    }

    override suspend fun saveWord(id: String) {
        // do nothing
    }

    override suspend fun deleteWord(id: String) {
        // do nothing
    }

    override suspend fun deleteAllWords() {
        // do nothing
    }

    override suspend fun isWordExist(id: String): Boolean {
        // do nothing
        return false
    }
}