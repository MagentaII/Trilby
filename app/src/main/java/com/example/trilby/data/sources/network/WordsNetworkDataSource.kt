package com.example.trilby.data.sources.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject

interface NetworkDataSource {
    suspend fun fetchWords(query: String): List<NetworkWords>
}

private const val SERVICE_LATENCY_IN_MILLIS = 2000L

class WordsNetworkDataSource @Inject constructor() : NetworkDataSource {

    private val accessMutex = Mutex()
    private val words = listOf(
        NetworkWords(
            id = "apple",
            headword = "apple",
            label = "noun",
            definition = "{bc} a round fruit with red, yellow, or green skin and firm white flesh"
        ),
        NetworkWords(
            id = "apple-cheeked",
            headword = "apple-cheeked",
            label = "adjective",
            definition = "{bc} having red or pink cheeks"
        ),
        NetworkWords(
            id = "apple polisher",
            headword = "apple polisher",
            label = "noun",
            definition = "{it}US{/it}, {it}informal + disapproving{/it} {bc} a person who tries to get the approval and friendship of someone in authority by praise, flattery, etc."
        ),
        NetworkWords(
            id = "Adam's apple",
            headword = "Adam's apple",
            label = "noun",
            definition = "{bc} the lump that sticks out in the front of a person's neck, that is usually larger in men than in women, and that moves when a person talks or swallows"
        )
    )

    override suspend fun fetchWords(query: String): List<NetworkWords> = accessMutex.withLock {
        delay(SERVICE_LATENCY_IN_MILLIS)
        return words
    }
}