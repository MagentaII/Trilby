package com.example.trilby.ui.screens.word_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.trilby.data.repositories.word_repository.ShowWord
import com.example.trilby.data.repositories.word_repository.WordPrs
import com.example.trilby.data.repositories.word_repository.WordRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class WordDetailUiState(
    val isFavorite: Boolean = false,
)

@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val wordRepository: WordRepository,
    private val exoPlayer: ExoPlayer,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WordDetailUiState())
    val uiState: StateFlow<WordDetailUiState> = _uiState.asStateFlow()


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
        viewModelScope.launch {
            wordRepository.saveWord(word = word)
            isWordExist(word)
        }
    }

    fun deleteWord(word: ShowWord) {
        viewModelScope.launch {
            wordRepository.deleteWord(word = word)
            isWordExist(word)
        }
    }

    fun isWordExist(word: ShowWord) {
        viewModelScope.launch {
            val isExist = wordRepository.isWordExist(word = word)
            _uiState.update { currentState ->
                currentState.copy(
                    isFavorite = isExist
                )
            }
        }
    }
}