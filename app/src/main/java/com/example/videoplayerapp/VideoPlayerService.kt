package com.example.videoplayerapp

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.icu.text.CaseMap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.support.v4.media.session.MediaSessionCompat
import androidx.annotation.MainThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.videoplayerapp.Application.Companion.CHANNEL_ID
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.util.NotificationUtil.createNotificationChannel

class VideoPlayerService : Service() {
    lateinit var videoList: ArrayList<VideoDataClass>
    private lateinit var player: MediaPlayer

   /* override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping = true
        player.start()
        return START_STICKY
    }*/
    private var myBinder = MyBinder()
    var mediaPlayer:ExoPlayer?=null

    private lateinit var  mediaSession : MediaSessionCompat
    override fun onBind(intent: Intent?): IBinder {
        mediaSession = MediaSessionCompat(baseContext, "Videos")
       return myBinder
    }
    inner class MyBinder : Binder(){
        fun currentService() : VideoPlayerService{
            return this@VideoPlayerService
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    fun showNotification(playpauseBtn: Int){

        //createNotificationChannel()

        val prevIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(Application.PREV)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext,0,prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(Application.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext,0,playIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(baseContext, NotificationReceiver::class.java).setAction(Application.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext,0,nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notificationManager: NotificationManager  = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "VIDEO_PLAYER"
            val descriptionText = "This is VideoPlayer channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(baseContext, Application.CHANNEL_ID)
            .setContentTitle(VideoPlayerActivity.playerList[VideoPlayerActivity.position].title)
            .setSmallIcon(R.drawable.video_image)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.folder))
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
           // .addAction(R.drawable.ic_previous,"Previous",prevPendingIntent)
            .addAction(playpauseBtn,"Play",playPendingIntent)
           // .addAction(R.drawable.ic_next,"Next",nextPendingIntent)
            .build()


        startForeground(13,notification)
        notificationManager.notify(13,notification)



    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "VIDEO_PLAYER"
            val descriptionText = "This is VideoPlayer channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onCreate() {
        super.onCreate()
      //  val trackSelection = AdaptiveTrackSelection.Factory(DefaultBandwidthMeter())

    }

    override fun onDestroy() {
        super.onDestroy()
       // player.stop()
    }
}