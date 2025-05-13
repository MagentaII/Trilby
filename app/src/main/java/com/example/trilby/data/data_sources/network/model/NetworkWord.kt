package com.example.trilby.data.data_sources.network.model

import com.example.trilby.data.repositories.word_repository.model.WordForUi
import com.example.trilby.data.repositories.word_repository.model.Word
import com.example.trilby.data.repositories.word_repository.model.WordPrs
import com.example.trilby.data.repositories.word_repository.model.WordSound
import com.example.trilby.data.repositories.word_repository.model.groupByWordId

data class NetworkWord(
    val meta: Meta,
    val hom: Int?,
    val hwi: Hwi,
    val fl: String,
    val def: List<Definition>,
    val shortdef: List<String>,
)

/**
 * Meta
 */
data class Meta(
    val id: String,
    val uuid: String,
    val section: String,
    val stems: List<String>,
)
/// ---------------------------------

/**
 * HeadwordInformation
 */
data class Hwi(
    val hw: String,
    val prs: List<Prs>?
)

data class Prs(
    val mw: String,
    val sound: Sound?
)

data class Sound(
    val audio: String,
    val ref: String,
    val stat: String
)
/// ---------------------------------

/**
 * Definition
 */
data class Definition(
    val sseq: List<List<List<Any>>>
)
/// ---------------------------------

fun List<NetworkWord>.toExternalModel(): List<WordForUi> {
    val wordList = this.map { it.toExternalModel() }
    return wordList.groupByWordId()
}

private fun NetworkWord.toExternalModel() = Word(
    wordId = meta.id,
//    wordUuid = meta.uuid,
    headword = hwi.hw,
    wordPrs = hwi.prs?.map { it.toExternalModel() },
    label = fl,
    shortDef = shortdef
)


private fun Prs.toExternalModel() = WordPrs(
    mw = mw,
    sound = sound?.toExternalModel()
)

private fun Sound.toExternalModel() = WordSound(
    audio = audio,
    ref = ref,
    stat = stat,
    subdirectory = audio.firstOrNull()?.toString() ?: ""
)