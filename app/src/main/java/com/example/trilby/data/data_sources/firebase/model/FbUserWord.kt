package com.example.trilby.data.data_sources.firebase.model

data class FbUserWord(
    val wordId: String = "",
    val addedAt: String = "",  // 或用 Timestamp
    val hasReview: Boolean = false
)
