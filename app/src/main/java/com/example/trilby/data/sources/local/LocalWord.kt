package com.example.trilby.data.sources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalWord(
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