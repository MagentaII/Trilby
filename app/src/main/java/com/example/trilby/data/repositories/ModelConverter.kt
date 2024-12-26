package com.example.trilby.data.repositories

import com.example.trilby.data.sources.local.LocalWord
import com.example.trilby.data.sources.network.AppShortDef
import com.example.trilby.data.sources.network.Meta
import com.example.trilby.data.sources.network.NetworkWord

// External to Local
fun Word.toLocal() = LocalWord(
    id = id,
    headword = headword,
    label = label,
    definition = definition,
)

@JvmName("ExternalToLocal")
fun List<Word>.toLocal() = map(Word::toLocal)

// Local to External
fun LocalWord.toExternal() = Word(
    id = id,
    headword = headword,
    label = label,
    definition = definition,
)

@JvmName("LocalToExternal")
fun List<LocalWord>.toExternal() = map(LocalWord::toExternal)

// External to Network
fun Word.toNetwork() = NetworkWord(
    meta = Meta(
        id = id,
        appShortDef = AppShortDef(
            headword = headword,
            label = label,
            definition = definition
        )
    )
)

@JvmName("ExternalToNetwork")
fun List<Word>.toNetwork() = map(Word::toNetwork)

// Network to External
fun NetworkWord.toExternal() = Word(
    id = meta.id,
    headword = meta.appShortDef.headword,
    label = meta.appShortDef.label,
    definition = meta.appShortDef.definition,
)

@JvmName("NetworkToExternal")
fun List<NetworkWord>.toExternal() = map(NetworkWord::toExternal)

// Local to Network
fun LocalWord.toNetwork() = toExternal().toNetwork()

@JvmName("LocalToNetwork")
fun List<LocalWord>.toNetwork() = map(LocalWord::toNetwork)

// Network to Local
fun NetworkWord.toLocal() = toExternal().toLocal()

@JvmName("NetworkToLocal")
fun List<NetworkWord>.toLocal() = map(NetworkWord::toLocal)