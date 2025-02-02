package com.example.trilby.data.sources.network.word_firestore_network_source

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface WordsFirestoreService {
    suspend fun addWord(words: List<FirestoreWord>)
    suspend fun getWord(): List<FirestoreWord>
}

class WordsFirestoreServiceImpl @Inject constructor(
    private val firebase: Firebase
) : WordsFirestoreService {

    override suspend fun addWord(words: List<FirestoreWord>) {
        Log.d("Firestore", "addWord: $words")
        val db = firebase.firestore

        words.forEach { firestoreWord ->
            val randomId = db.collection("words").document().id
            val mainDocData = mapOf("word" to firestoreWord.hwi.hw)
            db.collection("words")
                .document(randomId)
                .set(mainDocData)
                .addOnSuccessListener {
                    Log.d("Firestore", "Main document successfully written with ID: $randomId")
                    db.collection("words")
                        .document(randomId)
                        .collection(firestoreWord.hwi.hw)
                        .document(firestoreWord.uuid)
                        .set(firestoreWord)
                        .addOnSuccessListener {
                            Log.d(
                                "Firestore",
                                "Sub-document successfully written at /words/$randomId/${firestoreWord.hwi.hw}/${firestoreWord.uuid}"
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.w("Firestore", "Error writing sub-document", e)
                        }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Error writing main document", e)
                }
        }
    }


    override suspend fun getWord(): List<FirestoreWord> {
        val db = firebase.firestore
        val result = mutableListOf<FirestoreWord>()
        try {
            val querySnapshot = db.collection("words").get().await()
            for (document in querySnapshot) {
                Log.d("Firestore", "1${document.id} => ${document.data}")
                val word = document.data.values.first()
                val subQuerySnapshot =
                    document.reference.collection(word.toString()).get().await()
                for (subDocument in subQuerySnapshot) {
                    Log.d("Firestore", "2${subDocument.id} => ${subDocument.data}")
                    val firestoreWord = subDocument.toObject<FirestoreWord>()
                    Log.d("Firestore", "3${subDocument.id} => $firestoreWord")
                    result.add(firestoreWord)
                }
            }
        } catch (e: Exception) {
            Log.w("Firestore", "Error getting documents.", e)
            return emptyList()
        }
        return result
    }
}