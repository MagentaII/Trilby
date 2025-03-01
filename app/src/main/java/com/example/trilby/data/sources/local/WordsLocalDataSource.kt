package com.example.trilby.data.sources.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Insert
    suspend fun insertWord(word: LocalWord)

    @Insert
    suspend fun insertWords(words: List<LocalWord>)

    @Delete
    suspend fun deleteWords(words: List<LocalWord>)

    @Query("SELECT * FROM LocalWord")
    fun getAllWords(): Flow<List<LocalWord>>

    @Query("SELECT EXISTS(SELECT 1 FROM LocalWord WHERE id = :id)")
    suspend fun isWordExist(id: String): Boolean

    @Query("DELETE FROM LocalWord")
    suspend fun deleteAllWords()

    @Query("SELECT EXISTS(SELECT 1 FROM LocalWord LIMIT 1)")
    suspend fun hasWords(): Boolean
}

@Database(entities = [LocalWord::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE LocalWord ADD COLUMN word_prs TEXT")
            }
        }
    }
}