package com.example.trilby.data.sources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalWord(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "headword") val headword: String,
    @ColumnInfo(name = "function_label") val label: String,
    @ColumnInfo(name = "definition") val definition: List<String>,
)