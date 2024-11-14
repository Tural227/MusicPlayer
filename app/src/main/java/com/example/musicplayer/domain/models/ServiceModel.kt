package com.example.musicplayer.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ServiceModel(
    var path: String,
    var title: String,
    var duration: String,
    var artist: String,
) : Parcelable