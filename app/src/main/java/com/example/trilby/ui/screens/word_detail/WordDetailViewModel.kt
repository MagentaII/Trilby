package com.example.trilby.ui.screens.word_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.trilby.data.repositories.word_repository.WordRepository
import com.example.trilby.data.repositories.word_repository.model.ShowWord
import com.example.trilby.data.repositories.word_repository.model.WordPrs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = WordDetailViewModel.Factory::class)
class WordDetailViewModel @AssistedInject constructor(
    private val wordRepository: WordRepository,
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
                    val isExist = wordRepository.isWordExist(wordId)
                    _uiState.value = WordDetailUiState.Success(word, isExist)
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


    fun saveWord() {
        viewModelScope.launch {
            wordRepository.saveWord(wordId)
        }
    }

    fun deleteWord() {
        viewModelScope.launch {
            wordRepository.deleteWord(wordId)
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(
            wordId: String
        ): WordDetailViewModel
    }
}

sealed interface WordDetailUiState {
    data class Success(val word: ShowWord, val isFavorite: Boolean = false) : WordDetailUiState
    data object Loading : WordDetailUiState
    data class Error(val errorMessage: String) : WordDetailUiState
}