package com.example.trilby.data.sources.network.word_firestore_network_source

data class FirestoreWord(
    val uuid: String = "",
    val fl: String = "",
    val hwi: FirestoreHwi = FirestoreHwi(),
    val id: String = "",
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