package com.example.trilby.data.sources.network

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
    val prs: List<Prs>
)

data class Prs(
    val sound: Sound
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
    val sseq: List<List<List<Any>>>,
)

data class Sense(
    val sn: String,
)
/// ---------------------------------