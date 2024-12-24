package com.example.trilby.data.sources.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkWords(
    val id: String,
    @SerialName(value = "hw")
    val headword: String,
    @SerialName(value = "fl")
    val label: String,
    @SerialName(value = "def")
    val definition: String,
) {
    companion object {
        fun empty(): NetworkWords = NetworkWords(
            id = "",
            headword = "",
            label = "",
            definition = ""
        )
    }
}