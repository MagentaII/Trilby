package com.example.trilby.data.repositories.word_repository


data class ShowWord(
    val uid: String,
    val words: List<Word>,
) {
    companion object {
        val empty = ShowWord(
            uid = "",
            words = emptyList()
        )
    }
}

data class Word(
    val wordId: String,
    val wordUuid: String,
    val headword: String,
    val wordPrs: List<WordPrs>?,
    val label: String,
    val shortDef: List<String>,
) {
    companion object {
        val empty = Word(
            wordId = "",
            wordUuid = "",
            headword = "",
            wordPrs = null,
            label = "",
            shortDef = emptyList(),
        )
    }
}

data class WordPrs(
    val mw: String,
    val sound: WordSound?
)

data class WordSound(
    val audio: String,
    val ref: String,
    val stat: String,
    val subdirectory: String,
)