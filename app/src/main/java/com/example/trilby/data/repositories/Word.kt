package com.example.trilby.data.repositories

import kotlinx.serialization.Serializable

data class Word(
    val id: String,
    val headword: String,
    val label: String,
    val definition: List<String>,
) {
    companion object {
        val empty = Word(
            id = "",
            headword = "",
            label = "",
            definition = emptyList(),
        )
    }
}