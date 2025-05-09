package com.example.trilby.data.data_sources.firebase.firestore

import com.example.trilby.data.data_sources.firebase.WordFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.model.FirestoreWord
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseWordFirestore @Inject constructor(
    private val firebase: Firebase
) : WordFirebaseDataSource {

    override suspend fun insertWord(words: List<FirestoreWord>, userUid: String?) {
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
                        Timber.i("Firebase單字儲存成功: ${word.id}")
                    } else {
                        Timber.i("不儲存，Firebase單字已存在: ${word.id}")
                    }
                    wordIds.add(word.id)
                }
                val documentSnapshot = db.collection("user_words")
                    .document(userUid)
                    .get()
                    .await()
                if (documentSnapshot.exists()) {
                    val wordIdsFromFirestore = documentSnapshot.get("wordIds") as List<String>

                    if (!wordIdsFromFirestore.containsAll(wordIds)) {
                        db.collection("user_words")
                            .document(userUid)
                            .update("wordIds", FieldValue.arrayUnion(*wordIds.toTypedArray()))
                            .await()
                        Timber.i("Firebase user_words 更新成功: $userUid")
                    } else {
                        Timber.i("不更新，Firebase user_words 已存在: $userUid")
                    }
                } else {
                    db.collection("user_words")
                        .document(userUid)
                        .set(mapOf("wordIds" to wordIds))
                        .await()
                    Timber.i("Firebase user_words 儲存成功: $userUid")
                }
            } else {
                Timber.e("Firebase單字儲存失敗: userUid is null or empty")
            }
        } catch (e: Exception) {
            Timber.e("Firebase單字儲存失敗: $e")
        }
    }


    override suspend fun getAllWords(userUid: String?): List<FirestoreWord> {
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

    override suspend fun deleteWord(words: List<FirestoreWord>, userUid: String?) {
        val db = firebase.firestore
        try {
            if (!userUid.isNullOrEmpty()) {
                for (word in words) {
                    db.collection("user_words")
                        .document(userUid)
                        .update("wordIds", FieldValue.arrayRemove(word.id))
                        .await()
                }
                Timber.i("Firebase單字刪除成功: $userUid")
            } else {
                Timber.e("Firebase單字刪除失敗: userUid is null or empty")
            }
        } catch (e: Exception) {
            Timber.e("Firebase單字刪除失敗: $e")
        }
    }
}