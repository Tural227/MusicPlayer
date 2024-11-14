//package com.example.musicplayer.presentation.fragments.song_fragment
//
//import android.annotation.SuppressLint
//import android.content.ComponentName
//import android.graphics.BitmapFactory
//import android.graphics.Color
//import android.graphics.drawable.GradientDrawable
//import android.os.Bundle
//import android.util.Log
//
//import android.widget.SeekBar
//import android.widget.SeekBar.OnSeekBarChangeListener
//import androidx.activity.OnBackPressedCallback
//import androidx.annotation.OptIn
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.activityViewModels
//import androidx.lifecycle.lifecycleScope
//import androidx.media3.common.MediaItem
//import androidx.media3.common.MediaMetadata
//import androidx.media3.common.MediaMetadata.PICTURE_TYPE_FRONT_COVER
//import androidx.media3.common.Player
//import androidx.media3.common.util.UnstableApi
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.session.MediaController
//import androidx.media3.session.SessionToken
//import androidx.navigation.fragment.findNavController
//import androidx.palette.graphics.Palette
//import com.example.musicplayer.R
//import com.example.musicplayer.common.BaseFragment
//import com.example.musicplayer.databinding.SongLayoutBinding
//import com.example.musicplayer.domain.models.AudioModel
//import com.example.musicplayer.presentation.fragments.songs_fragment.SongsViewModel
//import com.example.musicplayer.servies.ThirdAttempt
//import com.google.common.util.concurrent.MoreExecutors
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.filter
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class SongFragment:BaseFragment<SongLayoutBinding>(SongLayoutBinding::inflate) {
//
//    val viewModel by activityViewModels<SongsViewModel>()
//    var mediaController : MediaController? = null
//
//
//    override fun main() {
//
//
//        checkIsPlaying()
//
//        val sessionToken = SessionToken(requireContext(), ComponentName(requireActivity(),ThirdAttempt::class.java))
//        val future = MediaController.Builder(requireContext(),sessionToken).buildAsync()
//
//
//
//
//        viewModel.future?.addListener({
//            mediaController = viewModel.future!!.get()
//
//            currentlyPlaying()
//
//            binding.pauseButton.setOnClickListener {
//                pauseMusic()
//
//            }
//
//            binding.skipForwardButton.setOnClickListener {
//                mediaController?.seekToNextMediaItem()
//            }
//
//
//            binding.skipBackwardButton.setOnClickListener{
//                mediaController?.seekToPreviousMediaItem()
//            }
//
//            binding.repeatButton.setOnClickListener {
//                if(mediaController?.repeatMode == Player.REPEAT_MODE_ONE){
//                    mediaController?.repeatMode = Player.REPEAT_MODE_ALL
//                }
//                else{
//                    mediaController?.repeatMode = Player.REPEAT_MODE_ONE
//
//                }
//
//            }
//
//            backStackInfo()
//
//
//
//
//        },
//            MoreExecutors.directExecutor())
//
//
//
//
//
//    }
//
//
//    @OptIn(UnstableApi::class)
//    fun currentlyPlaying(){
//        val mediaItem = mediaController?.currentMediaItem
//        if (mediaItem != null) {
//            setViewComponents(
//                AudioModel(
//                    title = mediaItem.mediaMetadata.title.toString(),
//                    duration = mediaItem.mediaMetadata.durationMs.toString(),
//                    artist = mediaItem.mediaMetadata.artist.toString(),
//                    albumArt = mediaItem.mediaMetadata.artworkData
//                )
//            )
//        }
//    }
//
//
//
//    fun backStackInfo(){
//        lifecycleScope.launch {
//            viewModel.songsState.collectLatest {songList->
//                viewModel.exoPlayer.addListener(object : Player.Listener{
//                    @OptIn(UnstableApi::class)
//                    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
//                        super.onMediaItemTransition(mediaItem, reason)
//                        mediaItem?.let {
//                        setViewComponents(
//                            AudioModel(
//                                title = mediaItem.mediaMetadata.title.toString(),
//                                duration = mediaItem.mediaMetadata.durationMs.toString(),
//                                artist = mediaItem.mediaMetadata.artist.toString(),
//                                albumArt = mediaItem.mediaMetadata.artworkData
//                            )
//                        )
//                        }
//
//                    }
//                })
//
//            }
//
//
//        }
//    }
//
//
//    @SuppressLint("ResourceType")
//    fun setViewComponents(audioModel: AudioModel){
//        if (audioModel.albumArt != null) {
//            val bitmap = BitmapFactory.decodeByteArray(audioModel.albumArt,0,audioModel.albumArt!!.size)
//            binding.imageView2.setImageBitmap(bitmap)
//            Palette.from(bitmap).generate { palette ->
//                palette?.let {
//                    val mutedColor = it.getMutedColor(Color.BLACK)
//
//                    val gradientDrawable = GradientDrawable(
//                        GradientDrawable.Orientation.TOP_BOTTOM,
//                        intArrayOf(mutedColor, Color.BLACK)
//                    )
//                    binding.root.background = gradientDrawable
//                }
//            }
//        }
//        binding.nameTextView.text = audioModel.title
//        binding.artistTextView.text = audioModel.artist
//        val seconds = audioModel.duration.toInt() / 1000
//        binding.durationTextView.text = String.format("%d:%02d",seconds/60,seconds%60)
//        Log.e("dura","${audioModel.duration}")
//
//        seekBar(audioModel)
//    }
//
//
//
//
//    fun checkIsPlaying(){
//        if(viewModel.exoPlayer.isPlaying){
//            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.pause_vector)
//            binding.pauseButton.icon = drawable
//        }
//        else{
//            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.pauser)
//            binding.pauseButton.icon = drawable
//        }
//    }
//
//
//
//
//    fun pauseMusic(){
//        if(mediaController!!.isPlaying){
//            mediaController?.pause()
//            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.pauser)
//            binding.pauseButton.icon = drawable
//
//        }else{
//            mediaController?.play()
//            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.pause_vector)
//            binding.pauseButton.icon = drawable
//
//        }
//    }
//
//    fun seekBar(audioModel: AudioModel){
//        binding.seekBar.max = audioModel.duration.toInt()
//
//        Log.e("test",binding.seekBar.max.toString())
//        binding.seekBar.progress = 0
//        lifecycleScope.launch {
//
//            while (true){
//                val currentTime = viewModel.exoPlayer.currentPosition / 1000
//                binding.seekBar.progress = viewModel.exoPlayer.currentPosition.toInt()
//                binding.currentDurationTextView.text = String.format("%d:%02d",currentTime/60,currentTime%60)
//                delay(100)
//            }
//
//
//
//        }
//
//
//
//        binding.seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener{
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                if(fromUser && seekBar!=null){
//                    viewModel.exoPlayer.seekTo(progress.toLong())
//                    val currentTime = progress / 1000
//                    binding.currentDurationTextView.text = String.format("%d:%02d",currentTime/60,currentTime%60)
//                }
//
//
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//
//            }
//        })
//    }
//
//
//    override fun buttonListener() {
//        super.buttonListener()
//        binding.backwardButton.setOnClickListener {
//            val result = Bundle().apply {
//                putString("msg","nigga")
//            }
//            parentFragmentManager.setFragmentResult("myKey",result)
//
//            findNavController().popBackStack()
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(this ,object : OnBackPressedCallback(true){
//            override fun handleOnBackPressed() {
//                val result = Bundle().apply {
//                    putString("msg","nigga")
//                }
//                parentFragmentManager.setFragmentResult("myKey",result)
//                findNavController().popBackStack()
//            }
//
//        })
//
//    }
//
//
//
//
//}