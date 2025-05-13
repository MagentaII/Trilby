package com.example.trilby.data.data_sources.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.trilby.data.repositories.word_repository.model.WordForUi
import com.example.trilby.data.repositories.word_repository.model.Word
import com.example.trilby.data.repositories.word_repository.model.WordPrs
import com.example.trilby.data.repositories.word_repository.model.groupByWordId
import com.google.gson.Gson

@Entity
data class WordEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "headword") val headword: String,
    @ColumnInfo(name = "word_prs") val wordPrs: List<String>?,
    @ColumnInfo(name = "function_label") val label: String,
    @ColumnInfo(name = "definition") val definition: List<String>,
)

data class LocalWordPrs(
    val mw: String,
    val sound: LocalWordSound?
)

data class LocalWordSound(
    val audio: String,
    val ref: String,
    val stat: String,
    val subdirectory: String,
)

fun List<WordEntity>.toExternalModel(): List<WordForUi> {
    val wordList = this.map { it.toExternalModel() }
    return wordList.groupByWordId()
}

private fun WordEntity.toExternalModel(): Word {
    val gson = Gson()
    val wordPrsList = wordPrs?.map { jsonString ->
        gson.fromJson(jsonString, WordPrs::class.java)
    }
    return Word(
        wordId = id,
//        wordUuid = "",
        headword = headword,
        wordPrs = wordPrsList,
        label = label,
        shortDef = definition
    )
}