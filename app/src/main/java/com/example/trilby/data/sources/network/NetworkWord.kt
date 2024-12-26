package com.example.trilby.data.sources.network

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkWord(
    val meta: Meta,
)

@Serializable
data class Meta(
    val id: String,
    @SerializedName(value = "app-shortdef")
    val appShortDef: AppShortDef,
)

@Serializable
data class AppShortDef(
    @SerializedName(value = "hw")
    val headword: String,
    @SerializedName(value = "fl")
    val label: String,
    @SerializedName(value = "def")
    val definition: List<String>,
)