package com.example.musicplayer.presentation.fragments.songs_fragment



import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import androidx.palette.graphics.Palette
import com.example.musicplayer.R
import com.example.musicplayer.databinding.SongsLayoutBinding
import com.example.musicplayer.domain.models.AudioModel
import com.example.musicplayer.presentation.fragments.songs_fragment.adapters.SongsAdapter
import com.example.musicplayer.servies.ThirdAttempt
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.math.abs


@AndroidEntryPoint
class SongsFragment: Fragment() {

    val viewModel by activityViewModels<SongsViewModel>()


    private  var mediaController: MediaController? = null



    var _binding : SongsLayoutBinding? = null
    val binding get() = _binding!!



    @UnstableApi
    val songsAdapter = SongsAdapter{ index->
        binding.root.getConstraintSet(R.id.start).apply {
            this.setVisibility(R.id.playerRoot,View.VISIBLE)
            binding.root.requestLayout()
        }

        mediaController!!.clearMediaItems()
        lifecycleScope.launch {


            viewModel.songsState.filter { it.isNotEmpty() }.collectLatest {songList->
                showMiniPlayer(
                    AudioModel(
                    title = songList[index].mediaMetadata.title.toString(),
                    duration = songList[index].mediaMetadata.durationMs.toString(),
                    artist = songList[index].mediaMetadata.artist.toString(),
                    albumArt = songList[index].mediaMetadata.artworkData
                ))


                mediaController!!.addMediaItem(
                    songList[index]
                )

                    val temp = mutableListOf<MediaItem>()
                    repeat(songList.size){
                        if(index > it ){
                            temp.add(songList[it])
                        }
                        else if(it > index){
                            mediaController!!.addMediaItem(
                                songList[it]
                            )
                        }
                    }
                    temp.forEach {
                        mediaController!!.addMediaItem(
                            it
                        )
                    }
                }
                        mediaController!!.play()
            }
        }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SongsLayoutBinding.inflate(inflater,container,false)
        return binding.root
    }

    private val listener = object: Player.Listener{
        @SuppressLint("UseCompatLoadingForDrawables")
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            if(isPlaying){
                val drawable = ContextCompat.getDrawable(requireContext(),R.drawable.pause_vector_white)
                val drawable2 = ContextCompat.getDrawable(requireContext(),R.drawable.pause_vector)

                binding.pauseButtonMain.background = drawable
                binding.pauseButton.icon = drawable2
            }
            else{
                val drawable = ContextCompat.getDrawable(requireContext(),R.drawable.pauser_white)
                val drawable2 = ContextCompat.getDrawable(requireContext(),R.drawable.pauser)
                binding.pauseButtonMain.background = drawable
                binding.pauseButton.icon = drawable2
            }
        }
    }

    @OptIn(UnstableApi::class)
    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.imageView5.setOnClickListener {
            if (binding.root.progress == 0f) {
                // Start the transition only if it's at the start
                binding.root.transitionToEnd()

                // Disable further clicks once the transition starts
                it.isClickable = false
            }
        }



        binding.songsRV.adapter = songsAdapter


        binding.backwardButton.setOnClickListener {
            binding.root.transitionToStart()
        }


        binding.root.setTransitionListener(object: MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }

            @RequiresApi(Build.VERSION_CODES.S)
            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                if(currentId == motionLayout?.startState){
                    binding.imageView5.isClickable = true
                }

            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })



        val gestureDetector = GestureDetector(requireContext(),SwipeDownGestureListener())
        binding.playerRoot.setOnTouchListener { v, event ->
            if(binding.root.currentState != binding.root.endState){
                gestureDetector.onTouchEvent(event)
            }

            true
        }




        val sessionToken = SessionToken(requireContext(), ComponentName(requireActivity(),ThirdAttempt::class.java))
        val future = MediaController.Builder(requireContext(),sessionToken).buildAsync()


        future.addListener({


            mediaController = future.get()
            observer()
            mediaController!!.addListener(listener)
            mediaController!!.repeatMode = Player.REPEAT_MODE_ALL


            binding.pauseButton.setOnClickListener {
                pauseMusic()
            }

            binding.pauseButtonMain.setOnClickListener {
                pauseMusic()
            }

            binding.skipBackwardButton.setOnClickListener {
                if(mediaController!!.repeatMode == Player.REPEAT_MODE_ALL){
                    mediaController!!.seekToNextMediaItem()

                }
            }

            binding.skipForwardButton.setOnClickListener {
                if(mediaController!!.repeatMode == Player.REPEAT_MODE_ALL){
                    mediaController!!.seekToPreviousMediaItem()

                }
            }

            binding.repeatButton.setOnClickListener {
                if(mediaController!!.repeatMode == Player.REPEAT_MODE_ONE){
                   mediaController!!.repeatMode = Player.REPEAT_MODE_ALL
                }
                else{
                    mediaController!!.repeatMode = Player.REPEAT_MODE_ONE
                }
            }

            binding.shuffleButton.setOnClickListener {
                mediaController!!.shuffleModeEnabled = true
                mediaController!!.seekToNextMediaItem()
            }


            mediaController!!.addListener(object : Player.Listener{
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    binding.pauseButtonMain.alpha = 0f
                    binding.seekBar2.alpha = 0f
                    mediaItem?.mediaMetadata?.let {
                        showMiniPlayer(
                            AudioModel(
                                title = it.title.toString(),
                                duration = it.durationMs.toString(),
                                artist = it.artist.toString(),
                                albumArt = it.artworkData
                            )
                        )

                    }



                }
            })

        },MoreExecutors.directExecutor())
    }



    private fun hideMiniPlayer(view : View) {
        val height = binding.playerRoot.height.toFloat()

        view.animate()
            .translationY(height)
            .alpha(0.0f)
            .setDuration(200)
            .withEndAction {
                binding.root.getConstraintSet(R.id.start).apply {
                    this.setVisibility(R.id.playerRoot,View.GONE)
                    binding.root.requestLayout()
                }
            }
            .start()

        mediaController?.pause()
    }

    private fun showVeryMiniPlayer(view: View){

        view.animate()
            .translationY(0f)
            .alpha(1.0f)
            .setDuration(300)
            .withEndAction {
                view.alpha = 1f

            }
            .start()

    }




    private fun showMiniPlayer(audioModel: AudioModel) {

        if(viewModel.bilmirem.not()){
            binding.root.getConstraintSet(R.id.start).setAlpha(R.id.playerRoot,1f)
            binding.root.getConstraintSet(R.id.start).setAlpha(R.id.imageView5,1f)
            binding.root.getConstraintSet(R.id.start).setAlpha(R.id.playerNameTextView,1f)
            binding.root.getConstraintSet(R.id.start).setAlpha(R.id.playerAuthorTextView,1f)
            binding.root.getConstraintSet(R.id.start).setAlpha(R.id.seekBar2,1f)
            binding.root.getConstraintSet(R.id.start).setAlpha(R.id.pauseButtonMain,1f)
            viewModel.bilmirem = true
        }


        showVeryMiniPlayer(binding.playerRoot)
        showVeryMiniPlayer(binding.imageView5)
        showVeryMiniPlayer(binding.playerNameTextView)
        showVeryMiniPlayer(binding.playerAuthorTextView)
        showVeryMiniPlayer(binding.seekBar2)
        showVeryMiniPlayer(binding.pauseButtonMain)
        binding.playerNameTextView.text = audioModel.artist
        binding.playerNameTextView.text = audioModel.title
        if(audioModel.albumArt != null){
            val bitmap = BitmapFactory.decodeByteArray(audioModel.albumArt,0, audioModel.albumArt!!.size)
            binding.imageView5.setImageBitmap(bitmap!!)
            Palette.from(bitmap).generate{palette->
                palette?.let{
                    val vibrantColor = it.getVibrantColor(Color.BLACK)
                    val mutedColor = it.getMutedColor(Color.BLACK)

                    val gradientDrawable = GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        intArrayOf(mutedColor,vibrantColor)
                    )
                    binding.playerRoot.background = gradientDrawable
                }
            }

        }else{
            binding.playerRoot.background = ContextCompat.getDrawable(requireContext(),R.drawable.mirta_bg)
            binding.imageView5.setImageResource(R.drawable.music_placeholder)
        }


        playMusic(audioModel)
    }












    fun observer(){
        lifecycleScope.launch {
            viewModel.songsState
                .filter { it.isNotEmpty() }
                .collectLatest {
                    songsAdapter.updateAdapter(it)
                }
        }

    }




    //change
    fun playMusic(audioModel: AudioModel){
        binding.seekBar2.progress = 0
        val duration = audioModel.duration.toInt() / 1000
        binding.durationTextView.text = String.format("%d:%02d",duration/60,duration%60)
        binding.seekBar2.max = audioModel.duration.toInt()
        binding.seekBar.max = audioModel.duration.toInt()
        seekBar()
        if(mediaController!!.duration.toString() == audioModel.duration){
            return
        }



    }

//    change
    fun pauseMusic(){
        if(mediaController!!.isPlaying){
            mediaController!!.pause()

        }else{
            mediaController!!.play()
        }
    }








    @SuppressLint("ClickableViewAccessibility")
    fun seekBar(){
        binding.seekBar2.setOnTouchListener { v, event ->  true}

        lifecycleScope.launch {
            while (true) {
                binding.seekBar2.progress = mediaController!!.currentPosition.toInt()
                binding.seekBar.progress = mediaController!!.currentPosition.toInt()
                val currentTime = mediaController!!.currentPosition / 1000
                binding.currentDurationTextView.text = String.format("%d:%02d",currentTime/60,currentTime%60)


                delay(100)
            }
        }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    mediaController!!.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })


    }





    inner class SwipeDownGestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,// birinci barmaq qoyulan yer
            e2: MotionEvent, // barmagin cekildiyi yer
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            if (e1 != null) {
                val deltaY = e2.y - e1.y // barmaqlar arasindaki mesafe
                if (deltaY > 100 && abs(velocityY) > 100) {
                    Log.e("NIGGA","")
                    hideMiniPlayer(binding.playerRoot)
                    hideMiniPlayer(binding.imageView5)
                    hideMiniPlayer(binding.playerNameTextView)
                    hideMiniPlayer(binding.playerAuthorTextView)
                    hideMiniPlayer(binding.seekBar2)
                    hideMiniPlayer(binding.pauseButtonMain)

                    return true
                }
            }
            return false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        mediaController?.clearMediaItems()
        mediaController?.removeListener(listener)
        mediaController?.release()
        mediaController = null
    }






}