package com.example.trilby.data.sources.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Dao
interface WordDao {

    @Insert
    suspend fun insertWord(word: LocalWord)

    @Delete
    suspend fun deleteWord(word: LocalWord)

    @Query("SELECT * FROM LocalWord")
    suspend fun getAllWords(): List<LocalWord>

    @Query("SELECT EXISTS(SELECT 1 FROM LocalWord WHERE id = :id)")
    suspend fun isWordExist(id: String): Boolean
}

@Database(entities = [LocalWord::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao
}