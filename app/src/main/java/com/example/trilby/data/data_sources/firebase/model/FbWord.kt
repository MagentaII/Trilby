package com.example.trilby.data.data_sources.firebase.model

import com.example.trilby.data.repositories.word_repository.model.WordForUi
import com.example.trilby.data.repositories.word_repository.model.Word
import com.example.trilby.data.repositories.word_repository.model.WordPrs
import com.example.trilby.data.repositories.word_repository.model.WordSound
import com.example.trilby.data.repositories.word_repository.model.groupByWordId

data class FbWord(
    val wordId: String = "",
    val fl: String = "",
    val hwi: FirestoreHwi = FirestoreHwi(),
//    val id: String = "",
    val shortdef: List<String> = emptyList()
)

data class FirestoreHwi(
    val hw: String = "",
    val prs: List<FirestorePrs>? = null
)

data class FirestorePrs(
    val mw: String = "",
    val sound: FirestoreSound? = null
)

data class FirestoreSound(
    val audio: String = "",
    val ref: String = "",
    val stat: String = ""
)

fun List<FbWord>.toExternalModel(): List<WordForUi> {
    val wordList = this.map(FbWord::toExternalModel)
    return wordList.groupByWordId()
}


private fun FbWord.toExternalModel() = Word(
    wordId = this.wordId,
//    wordUuid = uuid,
    headword = this.hwi.hw,
    wordPrs = this.hwi.prs?.map { it.toExternalModel() },
    label = this.fl,
    shortDef = this.shortdef
)

private fun FirestorePrs.toExternalModel() = WordPrs(
    mw = mw,
    sound = sound?.toExternalModel()
)

private fun FirestoreSound.toExternalModel() = WordSound(
    audio = audio,
    ref = ref,
    stat = stat,
    subdirectory = audio.firstOrNull()?.toString() ?: ""
)