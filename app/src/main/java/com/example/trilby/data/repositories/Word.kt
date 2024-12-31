package com.example.trilby.data.repositories

data class Word(
    val uid: String,
    val wordId: String,
    val headword: String,
    val label: String,
    val definition: List<String>,
) {
    companion object {
        val empty = Word(
            uid = "",
            wordId = "",
            headword = "",
            label = "",
            definition = emptyList(),
        )
    }
}

data class ShowWord(
    val uid: String,
    val wordId: List<String>,
    val headword: List<String>,
    val label: List<String>,
    val definition: List<List<String>>
) {
    companion object {
        val empty = ShowWord(
            uid = "",
            wordId = emptyList(),
            headword = emptyList(),
            label = emptyList(),
            definition = emptyList(),
        )
    }
}