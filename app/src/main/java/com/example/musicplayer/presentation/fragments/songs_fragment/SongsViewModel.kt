package com.example.musicplayer.presentation.fragments.songs_fragment


import android.content.Context
import android.media.MediaMetadata
import android.media.MediaPlayer

import android.view.autofill.AutofillId
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata.PICTURE_TYPE_FRONT_COVER
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.legacy.MediaBrowserCompat
import com.example.musicplayer.domain.models.AudioModel
import com.example.musicplayer.domain.usacases.FetchSongsUseCase
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SongsViewModel @Inject constructor(val context: Context): ViewModel() {
    val songsState = MutableStateFlow<List<MediaItem>>(listOf())

    var bilmirem = false

    init {

        songsState.update {
            FetchSongsUseCase(context.contentResolver,context).execute()
        }
    }
}