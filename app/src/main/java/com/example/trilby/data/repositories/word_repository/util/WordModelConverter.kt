package com.example.trilby.data.repositories.word_repository.util

import com.example.trilby.data.data_sources.database.model.WordEntity
import com.example.trilby.data.data_sources.database.model.LocalWordPrs
import com.example.trilby.data.data_sources.database.model.LocalWordSound
import com.example.trilby.data.data_sources.network.model.NetworkWord
import com.example.trilby.data.data_sources.network.model.Prs
import com.example.trilby.data.data_sources.network.model.Sound
import com.example.trilby.data.data_sources.firebase.model.FirestoreHwi
import com.example.trilby.data.data_sources.firebase.model.FirestorePrs
import com.example.trilby.data.data_sources.firebase.model.FirestoreSound
import com.example.trilby.data.data_sources.firebase.model.FirestoreWord
import com.example.trilby.data.repositories.word_repository.model.ShowWord
import com.example.trilby.data.repositories.word_repository.model.Word
import com.example.trilby.data.repositories.word_repository.model.WordPrs
import com.example.trilby.data.repositories.word_repository.model.WordSound
import com.google.gson.Gson

/**
 * External to Local
 */

fun ShowWord.toLocal(): List<WordEntity> {
    val words = List(this.words.size) { index ->
        Word(
            wordId = this.words[index].wordId,
            wordUuid = this.words[index].wordUuid,
            headword = this.words[index].headword,
            wordPrs = this.words[index].wordPrs,
            label = this.words[index].label,
            shortDef = this.words[index].shortDef,
        )
    }
    return words.toLocal()
}

fun Word.toLocal() = WordEntity(
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
fun List<Word>.toLocal(): List<WordEntity> = map(Word::toLocal)

/**
 * Local to External
 */
fun WordEntity.toExternal(): Word {
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
fun List<WordEntity>.toExternal(): List<Word> = map(WordEntity::toExternal)

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

//@JvmName("NetworkToExternal")
//fun List<NetworkWord>.toExternal(): List<Word> = map(NetworkWord::toExternal)
@JvmName("NetworkToExternal")
fun List<NetworkWord>.toExternal(): List<ShowWord> {
    val wordList = this.map { it.toExternal() }
    val showWordList = wordList.groupBy { it.wordId.substringBefore(":") }
        .map { (uid, words) -> ShowWord(uid = uid, words = words) }
    return showWordList
}

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

fun ShowWord.toFirestore(): List<FirestoreWord> {
    val firestoreWordList = this.words.map { it.toFirestore()}
    return firestoreWordList
}

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