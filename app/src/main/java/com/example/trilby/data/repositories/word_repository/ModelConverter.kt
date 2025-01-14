package com.example.trilby.data.repositories.word_repository

import com.example.trilby.data.sources.local.LocalWord
import com.example.trilby.data.sources.local.LocalWordPrs
import com.example.trilby.data.sources.local.LocalWordSound
import com.example.trilby.data.sources.network.word_network_source.NetworkWord
import com.example.trilby.data.sources.network.word_network_source.Prs
import com.example.trilby.data.sources.network.word_network_source.Sound
import com.google.gson.Gson

/**
 *  External to Local
 */
fun Word.toLocal() = LocalWord(
    id = wordId,
    headword = headword,
    wordPrs = wordPrs?.toLocalWordPrsAsJson(),
    label = label,
    definition = shortDef,
)

fun WordPrs.toLocalWordPrs(): LocalWordPrs {
    return LocalWordPrs(
        mw = mw,
        sound = sound?.toLocalWordSound()
    )
}

fun WordSound.toLocalWordSound(): LocalWordSound {
    return LocalWordSound(
        audio = audio,
        ref = ref,
        stat = stat,
        subdirectory = audio[0].toString()
    )
}

/**
 * List<External> to List<Local>
 */
@JvmName("ExternalToLocal")
fun List<Word>.toLocal() = map(Word::toLocal)

fun List<WordPrs>.toLocalWordPrs() = map(WordPrs::toLocalWordPrs)

fun List<WordPrs>.toLocalWordPrsAsJson(): List<String> {
    return map { wordPrs ->
        Gson().toJson(wordPrs.toLocalWordPrs()) // 将 LocalWordPrs 转换为 JSON 字符串
    }
}

// =====================================================================

/**
 * Local to External
 */
fun LocalWord.toExternal(): Word {
    val gson = Gson() // 或者使用你已有的 Gson 实例
    val wordPrsList: List<WordPrs>? = wordPrs?.map { jsonString ->
        gson.fromJson(jsonString, WordPrs::class.java) // 将 JSON 字符串转换为 WordPrs 对象
    }
    return Word(
        wordId = id,
        headword = headword,
        wordPrs = wordPrsList,
        label = label,
        shortDef = definition
    )
}

/**
 * List<Local> to List<External>
 */
@JvmName("LocalToExternal")
fun List<LocalWord>.toExternal() = map(LocalWord::toExternal)

// =====================================================================

/**
 * Network to External
 */
fun NetworkWord.toExternal(): Word {
    return Word(
        wordId = meta.id,
        headword = hwi.hw,
        wordPrs = hwi.prs?.toExternal(),
        label = fl,
        shortDef = shortdef,
    )
}

/**
 * Network.Prs to External.Prs
 */
fun Prs.toExternal(): WordPrs {
    return WordPrs(
        mw = mw,
        sound = sound?.toExternal()
    )
}

/**
 * List<Network.Prs> to List<External.Prs>
 */
@JvmName("NetworkPrsToExternal")
fun List<Prs>.toExternal() = map(Prs::toExternal)

/**
 * Network.Sound to External.Sound
 */
fun Sound.toExternal(): WordSound {
    return WordSound(
        audio = audio,
        ref = ref,
        stat = stat,
        subdirectory = audio[0].toString()
    )
}

/**
 * List<Network.Sound> to List<External.Sound>
 */
@JvmName("NetworkToExternal")
fun List<NetworkWord>.toExternal() = map(NetworkWord::toExternal)

// =====================================================================

// Network to Local
fun NetworkWord.toLocal() = toExternal().toLocal()

//@JvmName("NetworkToLocal")
fun List<NetworkWord>.toLocal() = map(NetworkWord::toLocal)

// =====================================================================