package com.example.trilby.data.sources.network.word_firestore_network_source

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface WordsFirestoreService {
    suspend fun saveWordToFirestore(words: List<FirestoreWord>, userUid: String?)
    suspend fun fetchAllWordFromFirestore(userUid: String?): List<FirestoreWord>
    suspend fun deleteWordForFirestore(words: List<FirestoreWord>, userUid: String?)
}

class WordsFirestoreServiceImpl @Inject constructor(
    private val firebase: Firebase
) : WordsFirestoreService {

    override suspend fun saveWordToFirestore(words: List<FirestoreWord>, userUid: String?) {
        Log.d("Firestore", "addWord: $words")
        val db = firebase.firestore
        val wordIds = mutableListOf<String>()
        try {
            if (!userUid.isNullOrEmpty()) {
                words.forEach { word ->
                    val documentSnapshot = db.collection("words").document(word.id).get().await()
                    if (!documentSnapshot.exists()) {
                        db.collection("words")
                            .document(word.id)
                            .set(word)
                            .await()
                        Log.i("Firestore", "saveWordToFirestore, words, Success: ${word.id}")
                    } else {
                        Log.i(
                            "Firestore",
                            "saveWordToFirestore, word has been exist in words: ${word.id}"
                        )
                    }
                    wordIds.add(word.id)
                }
                val documentSnapshot = db.collection("user_words")
                    .document(userUid)
                    .get()
                    .await()
                if (documentSnapshot.exists()) {
                    val wordIdsFromFirestore = documentSnapshot.get("wordIds") as List<String>
                    Log.i(
                        "Firestore",
                        "saveWordToFirestore, wordIdsFromFirestore: $wordIdsFromFirestore"
                    )

                    if (!wordIdsFromFirestore.containsAll(wordIds)) {
                        db.collection("user_words")
                            .document(userUid)
                            .update("wordIds", FieldValue.arrayUnion(*wordIds.toTypedArray()))
                            .await()
                        Log.i(
                            "Firestore",
                            "saveWordToFirestore, user_words, Success: $userUid"
                        )
                    } else {
                        Log.i(
                            "Firestore",
                            "saveWordToFirestore, word's id has been exist in user_words: $userUid, and ${wordIds[0]}"
                        )
                    }
                } else {
                    db.collection("user_words")
                        .document(userUid)
                        .set(mapOf("wordIds" to wordIds))
                        .await()
                    Log.i(
                        "Firestore",
                        "saveWordToFirestore, user_words, Success: $userUid"
                    )
                }
            } else {
                Log.e("Firestore", "saveWordToFirestore, Failure: userUid is null or empty")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "saveWordToFirestore, Failure: $e")
        }
    }


    override suspend fun fetchAllWordFromFirestore(userUid: String?): List<FirestoreWord> {
        val db = firebase.firestore
        val result = mutableListOf<FirestoreWord>()
        try {
            if (!userUid.isNullOrEmpty()) {
                val querySnapshot = db.collection("user_words")
                    .document(userUid)
                    .get()
                    .await()
                val wordIds = querySnapshot.get("wordIds") as List<String>
                wordIds.forEach { wordId ->
                    val documentSnapshot = db.collection("words")
                        .document(wordId)
                        .get()
                        .await()
                    documentSnapshot.toObject<FirestoreWord>()?.let { firestoreWord ->
                        result.add(firestoreWord)
                    }
                }
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return result
    }

    override suspend fun deleteWordForFirestore(words: List<FirestoreWord>, userUid: String?) {
        val db = firebase.firestore
        try {
            if (!userUid.isNullOrEmpty()) {
                for (word in words) {
                    db.collection("user_words")
                        .document(userUid)
                        .update("wordIds", FieldValue.arrayRemove(word.id))
                        .await()
                }
                Log.i("Firestore", "deleteWordForFirestore, Success: $userUid")
            } else {
                Log.e("Firestore", "deleteWordForFirestore, Failure: userUid is null or empty")
            }
        } catch (e: Exception) {
            Log.e("Firestore", "deleteWordForFirestore, Failure: $e")
        }
    }
}