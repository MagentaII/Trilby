package com.example.trilby.data.repositories.word_repository

import com.example.trilby.data.sources.local.LocalWord
import com.example.trilby.data.sources.local.LocalWordPrs
import com.example.trilby.data.sources.local.LocalWordSound
import com.example.trilby.data.sources.network.word_api_network_source.NetworkWord
import com.example.trilby.data.sources.network.word_api_network_source.Prs
import com.example.trilby.data.sources.network.word_api_network_source.Sound
import com.example.trilby.data.sources.network.word_firestore_network_source.FirestoreHwi
import com.example.trilby.data.sources.network.word_firestore_network_source.FirestorePrs
import com.example.trilby.data.sources.network.word_firestore_network_source.FirestoreSound
import com.example.trilby.data.sources.network.word_firestore_network_source.FirestoreWord
import com.google.gson.Gson

/**
 * External to Local
 */
fun Word.toLocal() = LocalWord(
    id = wordId,
    headword = headword,
    wordPrs = wordPrs?.toLocalWordPrsAsJson(),
    label = label,
    definition = shortDef,
)

fun WordPrs.toLocalWordPrs() = LocalWordPrs(
    mw = mw,
    sound = sound?.toLocalWordSound()
)

fun WordSound.toLocalWordSound() = LocalWordSound(
    audio = audio,
    ref = ref,
    stat = stat,
    subdirectory = audio.firstOrNull()?.toString() ?: ""
)

fun List<WordPrs>.toLocalWordPrsAsJson(): List<String> = map {
    Gson().toJson(it.toLocalWordPrs())
}

@JvmName("ExternalToLocal")
fun List<Word>.toLocal(): List<LocalWord> = map(Word::toLocal)

/**
 * Local to External
 */
fun LocalWord.toExternal(): Word {
    val gson = Gson()
    val wordPrsList = wordPrs?.map { jsonString ->
        gson.fromJson(jsonString, WordPrs::class.java)
    }
    return Word(
        wordId = id,
        wordUuid = "",
        headword = headword,
        wordPrs = wordPrsList,
        label = label,
        shortDef = definition
    )
}

@JvmName("LocalToExternal")
fun List<LocalWord>.toExternal(): List<Word> = map(LocalWord::toExternal)

/**
 * Network to External
 */
fun NetworkWord.toExternal() = Word(
    wordId = meta.id,
    wordUuid = meta.uuid,
    headword = hwi.hw,
    wordPrs = hwi.prs?.toExternalPrs(),
    label = fl,
    shortDef = shortdef
)

fun Prs.toExternalPrs() = WordPrs(
    mw = mw,
    sound = sound?.toExternalSound()
)

fun Sound.toExternalSound() = WordSound(
    audio = audio,
    ref = ref,
    stat = stat,
    subdirectory = audio.firstOrNull()?.toString() ?: ""
)

@JvmName("NetworkPrsToExternalPrs")
fun List<Prs>.toExternalPrs(): List<WordPrs> = map(Prs::toExternalPrs)

@JvmName("NetworkToExternal")
fun List<NetworkWord>.toExternal(): List<Word> = map(NetworkWord::toExternal)

//fun NetworkWord.toLocal() = toExternal().toLocal()
//fun List<NetworkWord>.toLocal(): List<LocalWord> = map(NetworkWord::toLocal)

/**
 * Firestore to External
 */
fun FirestoreWord.toExternal() = Word(
    wordId = id,
    wordUuid = uuid,
    headword = hwi.hw,
    wordPrs = hwi.prs?.toExternalPrs(),
    label = fl,
    shortDef = shortdef
)

fun FirestorePrs.toExternalPrs() = WordPrs(
    mw = mw,
    sound = sound?.toExternalSound()
)

fun FirestoreSound.toExternalSound() = WordSound(
    audio = audio,
    ref = ref,
    stat = stat,
    subdirectory = audio.firstOrNull()?.toString() ?: ""
)

@JvmName("FirestoreToExternal")
fun List<FirestoreWord>.toExternal(): List<Word> = map(FirestoreWord::toExternal)

@JvmName("FirestorePrsToExternalPrs")
fun List<FirestorePrs>.toExternalPrs(): List<WordPrs> = map(FirestorePrs::toExternalPrs)

/**
 * External to Firestore
 */
fun Word.toFirestore(): FirestoreWord {
    return FirestoreWord(
        fl = label,
        hwi = FirestoreHwi(
            hw = headword,
            prs = wordPrs?.toFirestorePrs()
        ),
        id = wordId,
        shortdef = shortDef
    )
}

fun WordPrs.toFirestorePrs(): FirestorePrs {
    return FirestorePrs(
        mw = mw,
        sound = sound?.toFirestoreSound()
    )
}

fun WordSound.toFirestoreSound(): FirestoreSound {
    return FirestoreSound(
        audio = audio,
        ref = ref,
        stat = stat
    )
}

@JvmName("ExternalPrsToFirestorePrs")
fun List<WordPrs>.toFirestorePrs() = map(WordPrs::toFirestorePrs)