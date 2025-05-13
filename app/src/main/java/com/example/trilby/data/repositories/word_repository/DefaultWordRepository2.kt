package com.example.trilby.data.repositories.word_repository

/**
class DefaultWordRepository2 @Inject constructor(
    private val wordNetworkDataSource: WordNetworkDataSource,
    private val wordDao: WordDao,
    private val wordsFirestoreService: WordFirebaseDataSource,
) : WordRepository {

    // Network
    private var words: List<ShowWord> = emptyList()

    override suspend fun searchWords(query: String): Flow<List<ShowWord>> = flow<List<ShowWord>> {
        Timber.d("開始搜尋單字: $query")
        val response = wordNetworkDataSource.getWordList(query = query).toExternal()
//        val grouped = response.groupBy { it.wordId.substringBefore(":") }
//            .map { (uid, words) -> ShowWord(uid = uid, words = words) }
        words = response
        emit(response)
    }.catch { e ->
        Timber.e(e, "搜尋單字失敗")
        emit(emptyList())
    }.flowOn(Dispatchers.IO)

    override fun getWordById(wordId: String): Flow<ShowWord> = flow {
        val word = words.find { it.uid == wordId }
        if (word != null) {
            emit(word)
        } else {
            throw NoSuchElementException("找不到對應單字：$wordId")
        }
    }.catch { e ->
        Timber.e(e, "獲取單字失敗")
        emit(ShowWord.empty)
    }

    // Local
    override suspend fun saveWordToLocal(showWord: ShowWord) {
        val words: List<Word> = List(showWord.words.size) { index ->
            Word(
                wordId = showWord.words[index].wordId,
                wordUuid = showWord.words[index].wordUuid,
                headword = showWord.words[index].headword,
                wordPrs = showWord.words[index].wordPrs,
                label = showWord.words[index].label,
                shortDef = showWord.words[index].shortDef,
            )
        }
        try {
            withContext(Dispatchers.IO) {
                wordDao.insertWords(words = words.toLocal())
            }
        } catch (e: Exception) {
            Timber.d("saveWord: $e")
        }
    }

    override suspend fun saveAllWordsToLocal(showWords: List<ShowWord>) {
        showWords.forEach { showWord ->
            val words: List<Word> = List(showWord.words.size) { index ->
                Word(
                    wordId = showWord.words[index].wordId,
                    wordUuid = showWord.words[index].wordUuid,
                    headword = showWord.words[index].headword,
                    wordPrs = showWord.words[index].wordPrs,
                    label = showWord.words[index].label,
                    shortDef = showWord.words[index].shortDef,
                )
            }
            try {
                withContext(Dispatchers.IO) {
                    wordDao.insertWords(words = words.toLocal())
                    Timber.d("saveAllWords: Success save word")
                }
            } catch (e: Exception) {
                Timber.d("saveAllWords: Failure save word, and $e")
            }

        }
    }

    override fun fetchAllWordsToLocal(userUid: String?): Flow<List<ShowWord>> {
        return flow {
            val haveWordsFromLocal = haveWordsInLocal()
            if (!haveWordsFromLocal) {
                Timber.d("fetchAllWordsToLocal: Room is empty")
                try {
                    if (!userUid.isNullOrEmpty()) {
                        val showWords = fetchAllWordFromFirestore(userUid)
                        saveAllWordsToLocal(showWords)
                    } else {
                        Timber.d("fetchAllWordsToLocal: userUid is empty")
                    }
                } catch (e: Exception) {
                    Timber.d("fetchAllWordsToLocal: getAllWords: $e")
                }
            }
            Timber.d("fetchAllWordsToLocal: Room is not empty")
            // 直接監聽 Room，轉換數據格式
            emitAll(
                wordDao.getAllWords().map { localWords ->
                    localWords.toExternal()
                        .groupBy { word -> word.wordId.substringBefore(":") }
                        .map { (uid, words) ->
                            ShowWord(
                                uid = uid,
                                words = words
                            )
                        }
                }
            )
        }
            .flowOn(Dispatchers.IO)
            .catch { e ->
                Timber.d("fetchAllWordsToLocal: Error processing data: $e")
                emit(emptyList())
            }
    }

    override suspend fun deleteWordForLocal(word: ShowWord) {
        val words: List<Word> = List(word.words.size) { index ->
            Word(
                wordId = word.words[index].wordId,
                wordUuid = word.words[index].wordUuid,
                headword = word.words[index].headword,
                wordPrs = word.words[index].wordPrs,
                label = word.words[index].label,
                shortDef = word.words[index].shortDef,
            )
        }
        try {
            withContext(Dispatchers.IO) {
                wordDao.deleteWords(words = words.toLocal())
            }
        } catch (e: Exception) {
            Timber.d("deleteWord: $e")
        }
    }

    override suspend fun isWordExistInLocal(word: ShowWord): Boolean {
        val words: List<Word> = List(word.words.size) { index ->
            Word(
                wordId = word.words[index].wordId,
                wordUuid = word.words[index].wordUuid,
                headword = word.words[index].headword,
                wordPrs = word.words[index].wordPrs,
                label = word.words[index].label,
                shortDef = word.words[index].shortDef,
            )
        }
        val isExist = try {
            withContext(Dispatchers.IO) {
                wordDao.isWordExist(id = words[0].toLocal().id)
            }
        } catch (e: Exception) {
            Timber.d("isWordExist: $e")
            false
        }
        return isExist
    }

    override suspend fun deleteAllWordsForLocal() {
        try {
            withContext(Dispatchers.IO) {
                wordDao.deleteAllWords()
                Timber.d("deleteAllWords: Success delete all words")
            }
        } catch (e: Exception) {
            Timber.d("deleteAllWords: Failure delete all words, and $e")
        }
    }

    override suspend fun haveWordsInLocal(): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val hasWords = wordDao.hasWords()
                Timber.d("hasWords: Success $hasWords")
                hasWords
            }
        } catch (e: Exception) {
            Timber.d("hasWords: Failure $e")
            false
        }
    }

    override suspend fun fetchAllWordFromFirestore(userUid: String?): List<ShowWord> {
        return try {
            withContext(Dispatchers.IO) {
                if (!userUid.isNullOrEmpty()) {
                    wordsFirestoreService.fetchAllWordFromFirestore(userUid = userUid)
                        .toExternal()
                        .groupBy { word -> word.wordId.substringBefore(":") }
                        .map { (uid, words) ->
                            ShowWord(
                                uid = uid,
                                words = words
                            )
                        }
                } else {
                    Timber.d("fetchAllWordFromFirestore: userUid is empty")
                    emptyList()
                }
            }
        } catch (e: Exception) {
            Timber.d("getFirestoreWord: $e")
            emptyList()
        }
    }

    override suspend fun saveWordToFirestore(showWord: ShowWord, userUid: String?) {
        val firestoreWords = showWord.words.map { word ->
            word.toFirestore()
        }
        try {
            withContext(Dispatchers.IO) {
                if (!userUid.isNullOrEmpty()) {
                    wordsFirestoreService.saveWordToFirestore(firestoreWords, userUid)
                } else {
                    Timber.d("addFirestoreWords: userUid is empty")
                }
            }
        } catch (e: Exception) {
            Timber.d("addFirestoreWords: $e")
        }

    }

    override suspend fun deleteWordForFirestore(showWord: ShowWord, userUid: String?) {
        val firestoreWords = showWord.words.map { word ->
            word.toFirestore()
        }
        try {
            withContext(Dispatchers.IO) {
                if (!userUid.isNullOrEmpty()) {
                    wordsFirestoreService.deleteWordForFirestore(firestoreWords, userUid)
                } else {
                    Timber.d("deleteFirestoreWords: userUid is empty")
                }
            }
        } catch (e: Exception) {
            Timber.d("deleteFirestoreWords: $e")
        }
    }
}
*/