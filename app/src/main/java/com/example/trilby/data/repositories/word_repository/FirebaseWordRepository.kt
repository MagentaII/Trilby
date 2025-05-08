package com.example.trilby.data.repositories.word_repository

import com.example.trilby.data.data_sources.firebase.WordFirebaseDataSource
import com.example.trilby.data.repositories.word_repository.model.ShowWord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseWordRepository @Inject constructor(
    private val wordsFirestoreService: WordFirebaseDataSource,
) : WordRepository {
    override fun getWords(): Flow<List<ShowWord>> {
        TODO("Not yet implemented")
    }

    override fun getWordById(id: String): Flow<ShowWord> {
        TODO("Not yet implemented")
    }

    override suspend fun saveWord(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteWord(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllWords() {
        TODO("Not yet implemented")
    }

    override suspend fun isWordExist(id: String): Boolean {
        TODO("Not yet implemented")
    }
}