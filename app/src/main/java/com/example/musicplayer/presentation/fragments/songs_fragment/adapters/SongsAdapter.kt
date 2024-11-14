package com.example.musicplayer.presentation.fragments.songs_fragment.adapters


import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import com.example.musicplayer.common.BaseAdapter
import com.example.musicplayer.databinding.SongItemBinding
import com.example.musicplayer.domain.models.AudioModel


class SongsAdapter(val onClick: (Int) -> Unit): BaseAdapter<MediaItem,SongItemBinding>(SongItemBinding::inflate) {
    @OptIn(UnstableApi::class)
    override fun onBindViewHolder(holder: BaseViewHolder<SongItemBinding>, position: Int) {
        val current = itemData.getOrNull(position)


        current?.let{
            val seconds = current.mediaMetadata.durationMs?.toInt()?.div(1000)
            holder.binding.nameTextView.text = current.mediaMetadata.title
            if(current.mediaMetadata.artworkData != null){
                val byteArray = current.mediaMetadata.artworkData
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray!!.size)
                holder.binding.imageView.setImageBitmap(bitmap)
            }
            holder.binding.authorTextView.text = current.mediaMetadata.artist
            if (seconds != null) {
                holder.binding.durationTextView.text = String.format("%d:%02d",seconds.div(60),seconds%60)
            }
            holder.binding.nameTextView.isSelected = true

            holder.binding.root.setOnClickListener {

                Log.e("position",position.toString())
                onClick(position)

            }
        }


    }


}