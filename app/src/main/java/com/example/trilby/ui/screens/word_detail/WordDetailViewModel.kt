package com.example.trilby.ui.screens.word_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.data.repositories.word_repository.model.ShowWord
import com.example.trilby.data.repositories.word_repository.model.WordPrs
import com.example.trilby.data.repositories.word_repository.WordRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


//data class WordDetailUiState(
//    val isFavorite: Boolean = false,
//)

@HiltViewModel(assistedFactory = WordDetailViewModel.Factory::class)
class WordDetailViewModel @AssistedInject constructor(
    private val wordRepository: WordRepository,
    private val authRepository: AuthRepository,
    private val exoPlayer: ExoPlayer,
    @Assisted val wordId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow<WordDetailUiState>(WordDetailUiState.Loading)
    val uiState: StateFlow<WordDetailUiState> = _uiState.asStateFlow()

    init {
        loadingWordDetail()
    }

    private fun loadingWordDetail() {
        viewModelScope.launch {
            _uiState.value = WordDetailUiState.Loading
            wordRepository.getWordById(wordId)
                .catch { e ->
                    _uiState.value = WordDetailUiState.Error("發生錯誤: ${e.localizedMessage}")
                }
                .collect { word ->
                    _uiState.value = WordDetailUiState.Success(word)
                }
        }
    }


    fun playWordAudio(wordPrs: WordPrs) {
        val wordAudioUrl =
            "https://media.merriam-webster.com/audio/prons/en/us/mp3/${wordPrs.sound?.subdirectory}/${wordPrs.sound?.audio}.mp3"
        Log.i("TAG", "playWordAudio, wordAudioUrl: $wordAudioUrl")
        val mediaItem = MediaItem.fromUri(wordAudioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    fun saveWord(word: ShowWord) {
//        viewModelScope.launch {
//            authRepository.getCurrentUserUid().collectLatest { userUid ->
//                wordRepository.saveWordToLocal(showWord = word)
//                isWordExist(word)
//                if (!userUid.isNullOrEmpty()) {
//                    wordRepository.saveWordToFirestore(showWord = word, userUid = userUid)
//                } else {
//                    Log.i("Room", "saveWord: userUid is empty")
//                }
//            }
//
//        }
    }

//    fun saveWord() {
//        viewModelScope.launch {
//            authRepository.getCurrentUserUid().collectLatest { userUid ->
//                wordRepository.saveWordToLocal(wordId = wordId)
//                isWordExist(wordId = wordId)
//                if (!userUid.isNullOrEmpty()) {
//                    wordRepository.saveWordToFirestore(wordId = wordId, userUid = userUid)
//                } else {
//                    Timber.i("Room, saveWord: userUid is empty")
//                }
//            }
//        }
//    }

    fun deleteWord(word: ShowWord) {
//        viewModelScope.launch {
//            authRepository.getCurrentUserUid().collectLatest { userUid ->
//                wordRepository.deleteWordForLocal(word = word)
//                isWordExist(word)
//                if (!userUid.isNullOrEmpty()) {
//                    wordRepository.deleteWordForFirestore(showWord = word, userUid = userUid)
//                } else {
//                    Log.i("Room", "deleteWord: userUid is empty")
//                }
//            }
//        }
    }

    fun isWordExist(word: ShowWord) {
//        viewModelScope.launch {
//            val isExist = wordRepository.isWordExistInLocal(word = word)
//            _uiState.update { currentState ->
//                currentState.copy(
//                    isFavorite = isExist
//                )
//            }
//        }
    }

    @AssistedFactory
    interface Factory {
        fun create(
            wordId: String
        ): WordDetailViewModel
    }
}

sealed interface WordDetailUiState {
    data class Success(val word: ShowWord) : WordDetailUiState
    data object Loading : WordDetailUiState
    data class Error(val errorMessage: String) : WordDetailUiState
}