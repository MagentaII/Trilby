package com.example.trilby.data.data_sources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trilby.data.data_sources.database.dao.WordDao
import com.example.trilby.data.data_sources.database.model.WordEntity
import com.example.trilby.data.data_sources.database.util.Converters

@Database(entities = [WordEntity::class], version = 2)
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