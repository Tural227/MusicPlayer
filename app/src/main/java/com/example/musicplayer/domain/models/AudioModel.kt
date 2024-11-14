package com.example.musicplayer.domain.models

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class AudioModel(var title: String, var duration: String, var artist: String ,var albumArt : ByteArray?) : Parcelable