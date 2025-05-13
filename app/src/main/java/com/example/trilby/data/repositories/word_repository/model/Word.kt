package com.example.trilby.data.repositories.word_repository.model

import com.example.trilby.data.data_sources.database.model.LocalWordPrs
import com.example.trilby.data.data_sources.database.model.LocalWordSound
import com.example.trilby.data.data_sources.database.model.WordEntity
import com.example.trilby.data.data_sources.firebase.model.FbWord
import com.example.trilby.data.data_sources.firebase.model.FirestoreHwi
import com.example.trilby.data.data_sources.firebase.model.FirestorePrs
import com.example.trilby.data.data_sources.firebase.model.FirestoreSound
import com.google.gson.Gson


data class WordForUi(
    val wordId: String,
    val words: List<Word>,
) {
    companion object {
        val empty = WordForUi(
            wordId = "",
            words = emptyList()
        )
    }
}

data class Word(
    val wordId: String,
//    val wordUuid: String,
    val headword: String,
    val wordPrs: List<WordPrs>?,
    val label: String,
    val shortDef: List<String>,
) {
    companion object {
        val empty = Word(
            wordId = "",
//            wordUuid = "",
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

fun List<Word>.groupByWordId(): List<WordForUi> {
    return this.groupBy { it.wordId.substringBefore(":") }
        .map { (wordId, words) -> WordForUi(wordId, words) }
}

fun WordForUi.toFirebaseModel(): List<FbWord> {
    val firestoreWordList = this.words.map { it.toFirebaseModel() }
    return firestoreWordList
}

fun WordForUi.toEntity(): List<WordEntity> {
    val words = List(this.words.size) { index ->
        Word(
            wordId = this.words[index].wordId,
//            wordUuid = this.words[index].wordUuid,
            headword = this.words[index].headword,
            wordPrs = this.words[index].wordPrs,
            label = this.words[index].label,
            shortDef = this.words[index].shortDef,
        )
    }
    return words.map { it.toEntity() }
}

private fun Word.toFirebaseModel(): FbWord {
    return FbWord(
        wordId = this.wordId,
        fl = this.label,
        hwi = FirestoreHwi(
            hw = this.headword,
            prs = this.wordPrs?.map { it.toFirebaseModel() }
        ),
//        id = wordId,
        shortdef = this.shortDef
    )
}

private fun WordPrs.toFirebaseModel(): FirestorePrs {
    return FirestorePrs(
        mw = mw,
        sound = sound?.toFirebaseModel()
    )
}

private fun WordSound.toFirebaseModel(): FirestoreSound {
    return FirestoreSound(
        audio = audio,
        ref = ref,
        stat = stat
    )
}

private fun Word.toEntity() = WordEntity(
    id = this.wordId,
    headword = this.headword,
    wordPrs = this.wordPrs?.map { Gson().toJson(it.toEntity()) },
    label = this.label,
    definition = this.shortDef,
)

private fun WordPrs.toEntity() = LocalWordPrs(
    mw = mw,
    sound = sound?.toEntity()
)

private fun WordSound.toEntity() = LocalWordSound(
    audio = audio,
    ref = ref,
    stat = stat,
    subdirectory = audio.firstOrNull()?.toString() ?: ""
)