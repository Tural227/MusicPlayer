package com.example.musicplayer.domain.usacases

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import com.example.musicplayer.R
import com.example.musicplayer.domain.models.AudioModel
import java.io.ByteArrayOutputStream
import java.io.File
import kotlin.math.log

class FetchSongsUseCase(private val contentResolver: ContentResolver, private val context : Context) {

    @OptIn(UnstableApi::class)
    fun execute(): List<MediaItem> {
        val songsList = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ARTIST
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )

        cursor?.use {
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)



            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.music_placeholder)
            val defaultIcon = bitmapToByteArray(bitmap)


            while (cursor.moveToNext()) {
                val filePath = cursor.getString(dataColumn)

                Log.e("filePath",filePath.toString())

                val albumArt = getAlbumArt(filePath)


                if(!cursor.getString(dataColumn).contains("WhatsApp Audio")){
                    val songData = MediaItem.Builder().setUri(filePath)
                        .setMediaMetadata(
                            androidx.media3.common.MediaMetadata.Builder()
                                .setTitle(cursor.getString(titleColumn))
                                .setArtist(cursor.getString(artistColumn))
                                .setDurationMs(cursor.getString(durationColumn).toLong())
                                .setArtworkData(albumArt, MediaMetadata.PICTURE_TYPE_FRONT_COVER)
                                .build()
                        )
                        .build()

                    if (File(filePath).exists()) {
                        songsList.add(songData)
                    }
                }

            }
        }

        return songsList
    }

    fun bitmapToByteArray(bitmap: Bitmap, format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(format, quality, outputStream)
        return outputStream.toByteArray()
    }

    private fun getAlbumArt(filePath: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(filePath)
            val embeddedPicture = retriever.embeddedPicture
            embeddedPicture
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            retriever.release()
        }
    }
}
