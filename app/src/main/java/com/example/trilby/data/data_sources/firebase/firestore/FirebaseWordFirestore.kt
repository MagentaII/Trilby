package com.example.trilby.data.data_sources.firebase.firestore

import com.example.trilby.data.data_sources.firebase.WordFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.model.FbWord
import com.example.trilby.data.data_sources.firebase.model.FbUserWord
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseWordFirestore @Inject constructor(
    private val firebase: Firebase
) : WordFirebaseDataSource {
    private val db = firebase.firestore

    override suspend fun insertWord(userId: String, wordList: List<FbWord>) {
        try {
            val batch = db.batch()
            val userWordsRef = db
                .collection("users")
                .document(userId)
                .collection("words")

            wordList.forEach { firestoreWord ->
                val wordRef = userWordsRef.document(firestoreWord.wordId)
                batch.set(
                    wordRef, mapOf(
                        "wordId" to firestoreWord.wordId,
                        "addedAt" to FieldValue.serverTimestamp(),
                        "hasReview" to false,
                    )
                )
            }

            val dictionaryRef = db.collection("dictionary")
            wordList.forEach { firestoreWord ->
                val wordRef = dictionaryRef.document(firestoreWord.wordId)
                batch.set(wordRef, firestoreWord)
            }

            batch.commit()
                .addOnSuccessListener { Timber.d("批次寫入成功") }
                .addOnFailureListener { e -> Timber.e("批次寫入失敗: $e") }

            Timber.i("Firebase單字儲存成功: $userId")
        } catch (e: Exception) {
            Timber.e("Firebase單字儲存失敗: $e")
        }
    }

    override fun getAllWords(userId: String): Flow<List<FbWord>> = callbackFlow {
        val userWordsListener = db
            .collection("users")
            .document(userId)
            .collection("words")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val userWordIds = snapshot?.documents
                    ?.mapNotNull { it.toObject(FbUserWord::class.java)?.wordId }
                    ?.takeIf { it.isNotEmpty() } ?: run {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val chunks = userWordIds.chunked(10)
                val allWords = mutableListOf<FbWord>()

                chunks.forEach { ids ->
                    db.collection("dictionary")
                        // 因為最多只能10筆，所以需要將資料以10個單位劃分
                        .whereIn(FieldPath.documentId(), ids)
                        .get()
                        .addOnSuccessListener { dictSnapshot ->
                            val dictWords = dictSnapshot.documents.mapNotNull {
                                it.toObject(FbWord::class.java)
                            }
                            allWords.addAll(dictWords)

                            // 當所有查詢都完成才發送資料
                            if (allWords.size >= userWordIds.size || allWords.size == snapshot.size()) {
                                trySend(allWords)
                            }
                        }
                }
            }

        awaitClose {
            userWordsListener.remove()
        }
    }

    override suspend fun deleteWord(userId: String, wordList: List<FbWord>) {
        wordList.forEach { word ->
            val wordRef = db
                .collection("users")
                .document(userId)
                .collection("words")
                .document(word.wordId)

            wordRef.delete()
                .addOnSuccessListener {
                    Timber.d("刪除成功：${word.wordId}")
                }
                .addOnFailureListener { e ->
                    Timber.e("刪除失敗：$e")
                }
        }

    }

    override suspend fun deleteAllWords(userId: String) {
        val userWordsRef = db
            .collection("users")
            .document(userId)
            .collection("words")

        try {
            // 取得所有 documents
            val snapshot = userWordsRef.get().await()

            // 開始批次刪除
            val batch = db.batch()
            snapshot.documents.forEach { doc ->
                batch.delete(doc.reference)
            }

            // 提交刪除操作
            batch.commit().await()
            Timber.d("成功刪除 ${snapshot.size()} 筆單字")
        } catch (e: Exception) {
            Timber.e("刪除使用者單字時發生錯誤：$e")
        }
    }

    override suspend fun isWordExist(userId: String, wordList: List<FbWord>): Result<Boolean> {
        val wordIds = wordList.map { it.wordId }
        val userWordsRef = db
            .collection("users")
            .document(userId)
            .collection("words")

        return try {
            // 分批查詢，每批最多10筆
            val chunks = wordIds.chunked(10)
            val foundIds = mutableSetOf<String>()

            for (chunk in chunks) {
                val snapshot = userWordsRef
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                snapshot.documents.mapNotNullTo(foundIds) { it.id }
            }

            // 判斷是否所有要查的 ID 都已存在
            val allExist = wordIds.all { it in foundIds }
            Result.success(allExist)

        } catch (e: Exception) {
            Timber.e(e, "檢查單字是否存在時發生錯誤")
            Result.failure(e)
        }
    }

    override suspend fun getWord(userId: String, wordList: List<FbWord>): List<FbWord> {
        val wordIds = wordList.map { it.wordId }.distinct()
        val dictionaryRef = db.collection("dictionary")
        val result = mutableListOf<FbWord>()

        try {
            val chunks = wordIds.chunked(10)  // Firestore 限制一次最多 10 個 ID

            for (chunk in chunks) {
                val snapshot = dictionaryRef
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                val words = snapshot.documents.mapNotNull { it.toObject(FbWord::class.java) }
                result.addAll(words)
            }

            return result

        } catch (e: Exception) {
            Timber.e("取得單字資料失敗: $e")
            return emptyList()
        }
    }

//    override suspend fun insertWord(words: List<FirestoreWord>, userUid: String?) {
////        val db = firebase.firestore
//        val wordIds = mutableListOf<String>()
//        try {
//            if (!userUid.isNullOrEmpty()) {
//                words.forEach { word ->
//                    val documentSnapshot = db.collection("words").document(word.id).get().await()
//                    if (!documentSnapshot.exists()) {
//                        db.collection("words")
//                            .document(word.id)
//                            .set(word)
//                            .await()
//                        Timber.i("Firebase單字儲存成功: ${word.id}")
//                    } else {
//                        Timber.i("不儲存，Firebase單字已存在: ${word.id}")
//                    }
//                    wordIds.add(word.id)
//                }
//                val documentSnapshot = db.collection("user_words")
//                    .document(userUid)
//                    .get()
//                    .await()
//                if (documentSnapshot.exists()) {
//                    val wordIdsFromFirestore = documentSnapshot.get("wordIds") as List<String>
//
//                    if (!wordIdsFromFirestore.containsAll(wordIds)) {
//                        db.collection("user_words")
//                            .document(userUid)
//                            .update("wordIds", FieldValue.arrayUnion(*wordIds.toTypedArray()))
//                            .await()
//                        Timber.i("Firebase user_words 更新成功: $userUid")
//                    } else {
//                        Timber.i("不更新，Firebase user_words 已存在: $userUid")
//                    }
//                } else {
//                    db.collection("user_words")
//                        .document(userUid)
//                        .set(mapOf("wordIds" to wordIds))
//                        .await()
//                    Timber.i("Firebase user_words 儲存成功: $userUid")
//                }
//            } else {
//                Timber.e("Firebase單字儲存失敗: userUid is null or empty")
//            }
//        } catch (e: Exception) {
//            Timber.e("Firebase單字儲存失敗: $e")
//        }
//    }


//    override suspend fun getAllWords(userUid: String?): List<FirestoreWord> {
//        val db = firebase.firestore
//        val result = mutableListOf<FirestoreWord>()
//        try {
//            if (!userUid.isNullOrEmpty()) {
//                val querySnapshot = db.collection("user_words")
//                    .document(userUid)
//                    .get()
//                    .await()
//                val wordIds = querySnapshot.get("wordIds") as List<String>
//                wordIds.forEach { wordId ->
//                    val documentSnapshot = db.collection("words")
//                        .document(wordId)
//                        .get()
//                        .await()
//                    documentSnapshot.toObject<FirestoreWord>()?.let { firestoreWord ->
//                        result.add(firestoreWord)
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            return emptyList()
//        }
//        return result
//    }

//    override suspend fun deleteWord(words: List<FbWord>, userUid: String?) {
//        val db = firebase.firestore
//        try {
//            if (!userUid.isNullOrEmpty()) {
//                for (word in words) {
//                    db.collection("user_words")
//                        .document(userUid)
//                        .update("wordIds", FieldValue.arrayRemove(word.id))
//                        .await()
//                }
//                Timber.i("Firebase單字刪除成功: $userUid")
//            } else {
//                Timber.e("Firebase單字刪除失敗: userUid is null or empty")
//            }
//        } catch (e: Exception) {
//            Timber.e("Firebase單字刪除失敗: $e")
//        }
//    }
}