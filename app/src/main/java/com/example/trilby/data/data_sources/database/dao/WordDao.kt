package com.example.trilby.data.data_sources.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.trilby.data.data_sources.database.model.WordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

//    @Insert
//    suspend fun insertWord(word: WordEntity)

    @Insert
    suspend fun insertWords(words: List<WordEntity>)

    @Delete
    suspend fun deleteWords(words: List<WordEntity>)

    @Query("SELECT * FROM WordEntity")
    fun getAllWords(): Flow<List<WordEntity>> // 即時反應資料庫變化

    @Query("SELECT * FROM WordEntity WHERE id LIKE :id || '%'")
    fun getWordById(id: String): List<WordEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM WordEntity WHERE id = :id)")
    suspend fun isWordExist(id: String): Boolean

    @Query("DELETE FROM WordEntity")
    suspend fun deleteAllWords()

    @Query("SELECT EXISTS(SELECT 1 FROM WordEntity LIMIT 1)")
    suspend fun hasWords(): Boolean
}