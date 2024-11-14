package com.example.musicplayer.servies



import android.content.Intent
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ThirdAttempt : MediaSessionService() {

    var exoPlayer: ExoPlayer? = null

    private var mediaSession: MediaSession? = null


    @OptIn(UnstableApi::class)
    override fun onCreate() {
        super.onCreate()

        exoPlayer = ExoPlayer.Builder(this).build()


        exoPlayer?.let {
            mediaSession = MediaSession.Builder(this,it).build()

            it.prepare()
            it.play()

        }



    }



    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSession?.release()
        mediaSession = null
        exoPlayer = null
    }

}
