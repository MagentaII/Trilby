package com.example.trilby.ui.screens.word_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.trilby.data.repositories.word_repository.WordPrs
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class WordDetailViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer,
) : ViewModel() {

    fun playWordAudio(wordPrs: WordPrs) {
        val wordAudioUrl =
            "https://media.merriam-webster.com/audio/prons/en/us/mp3/${wordPrs.sound?.subdirectory}/${wordPrs.sound?.audio}.mp3"
        Log.i("TAG", "playWordAudio, wordAudioUrl: $wordAudioUrl")
        val mediaItem = MediaItem.fromUri(wordAudioUrl)
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }
}