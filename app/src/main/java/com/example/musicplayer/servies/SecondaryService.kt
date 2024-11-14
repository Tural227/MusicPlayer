//package com.example.musicplayer.servies
//
//import android.annotation.SuppressLint
//import android.media.browse.MediaBrowser.MediaItem
//import androidx.annotation.OptIn
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.session.MediaSession
//import androidx.media3.session.MediaSessionService
//import androidx.media3.session.legacy.MediaSessionCompat
//import androidx.media3.session.legacy.PlaybackStateCompat
//import javax.inject.Inject
//
//class SecondaryService: MediaSessionService() {
//
//    @SuppressLint("RestrictedApi")
//    @UnstableApi
//    private var mediaSession : MediaSessionCompat? = null
//
//    @Inject
//    lateinit var exoPlayer: ExoPlayer
//
//
//    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
//        return mediaSession
//    } // after onCreate()
//
//    @SuppressLint("RestrictedApi")
//    @OptIn(UnstableApi::class)
//    override fun onCreate() {//first in lifecycle
//        super.onCreate()
//        mediaSession = MediaSessionCompat(this,"SecondaryService").apply {
//            setCallback(mediaSessionCallback)
//            setSessionToken(sessionToken)
//            isActive = true
//        }
//    }
//
//    @SuppressLint("RestrictedApi")
//    @OptIn(UnstableApi::class)
//    override fun onDestroy() {
//        exoPlayer.release()
//        mediaSession?.release()
//        mediaSession = null
//        super.onDestroy()
//    }
//    @SuppressLint("RestrictedApi")
//    @UnstableApi
//    private val mediaSessionCallback = object : MediaSessionCompat.Callback() {
//        override fun onPlay() {
//            exoPlayer.play()
//            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING)
//        }
//
//        override fun onPause() {
//            exoPlayer.pause()
//            updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
//        }
//
//        override fun onStop() {
//            exoPlayer.stop()
//            updatePlaybackState(PlaybackStateCompat.STATE_STOPPED)
//        }
//
//        override fun onSeekTo(pos: Long) {
//            exoPlayer.seekTo(pos)
//            updatePlaybackState(PlaybackStateCompat.STATE_PLAYING)
//        }
//
//        override fun onPrepare() {
//            val mediaItem = androidx.media3.common.MediaItem.fromUri("your-media-uri-here")
//            exoPlayer.setMediaItem(mediaItem)
//            exoPlayer.prepare()
//            updatePlaybackState(PlaybackStateCompat.STATE_BUFFERING)
//        }
//    }
//
//    @SuppressLint("RestrictedApi")
//    @OptIn(UnstableApi::class)
//    private fun updatePlaybackState(state: Int) {
//        val playbackState = PlaybackStateCompat.Builder()
//            .setActions(
//                PlaybackStateCompat.ACTION_PLAY or
//                        PlaybackStateCompat.ACTION_PAUSE or
//                        PlaybackStateCompat.ACTION_STOP or
//                        PlaybackStateCompat.ACTION_SEEK_TO
//            )
//            .setState(state, exoPlayer.currentPosition, 1.0f)
//            .build()
//        mediaSession.setPlaybackState(playbackState)
//    }
//
//}