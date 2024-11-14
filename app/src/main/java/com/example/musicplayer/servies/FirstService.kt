//package com.example.musicplayer.servies
//
//import android.annotation.SuppressLint
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Context
//import android.content.Intent
//import android.media.MediaPlayer
//import android.os.Build
//import android.os.IBinder
//import android.util.Log
//import android.widget.RemoteViews
//import androidx.annotation.RequiresApi
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.example.musicplayer.R
//import com.example.musicplayer.domain.models.AudioModel
//import com.example.musicplayer.domain.models.ServiceModel
//import dagger.hilt.android.AndroidEntryPoint
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//@AndroidEntryPoint
//class FirstService : Service() {
//
//    private val counter = Counter()
//    val channelID = "31"
//    private lateinit var notificationLayout: RemoteViews
//
//    @Inject
//    lateinit var mediaPlayer: MediaPlayer
//
//    override fun onBind(intent: Intent): IBinder? {
//        return null
//    }
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        notificationLayout = RemoteViews(packageName,R.layout.music_notification)
//
//        val serviceModel = intent?.getParcelableExtra("serviceModel",ServiceModel::class.java)
//
//
//
//        val playPauseIntent = Intent(this, FirstService::class.java).apply {
//            action = CounterAction.ACTION_PLAY_PAUSE.name
//
////        }
//        val playPausePendingIntent = PendingIntent.getService(
//            this,
//            0,
//            playPauseIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//        notificationLayout.setOnClickPendingIntent(R.id.notButton1, playPausePendingIntent)
//
//
//        when(intent?.action){
//            CounterAction.ACTION_PLAY_PAUSE.name->{if(mediaPlayer.isPlaying)
//                mediaPlayer.pause()
//                else mediaPlayer.start()
//             }
//
//            CounterAction.START.name -> {
//                createNotificationChannel()
//
//                start()
//            }
//
//            CounterAction.STOP.name -> stop()
//        }
//
//
//        return super.onStartCommand(intent, flags, startId)
//    }
//    @RequiresApi(Build.VERSION_CODES.S)
//    private fun start(){
//        CoroutineScope(Dispatchers.Main).launch {
//            while (true){
//                delay(500)
//                notification()
//            }
//        }
//
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private fun notification(){
//        if(mediaPlayer.isPlaying){
//            notificationLayout.setInt(R.id.notButton1,"setBackgroundResource",R.drawable.pause_vector)
//        }
//        else{
//            notificationLayout.setInt(R.id.notButton1,"setBackgroundResource", R.drawable.pauser)
//        }
//
//        if(mediaPlayer.isPlaying){
//            notificationLayout.setProgressBar(R.id.progressBar2,mediaPlayer.duration,mediaPlayer.currentPosition,false)
//        }
//
//        val builder = NotificationCompat
//            .Builder(this, channelID)
//            .setCustomContentView(notificationLayout)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setCustomBigContentView(notificationLayout)
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setOnlyAlertOnce(true)
//
//
//        NotificationManagerCompat.from(this).notify(1,builder.build())
//    }
//
//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = "GOT"
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val channel = NotificationChannel(channelID, name, importance)
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//    private fun stop(){
//        counter.stopCounter()
//        stopSelf()
//    }
//
//
//    enum class CounterAction{
//        START,
//        STOP,
//        ACTION_PLAY_PAUSE,
//    }
//
//}