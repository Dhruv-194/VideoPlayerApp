package com.example.videoplayerapp

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.provider.MediaStore
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.example.videoplayerapp.databinding.ActivityVideoPlayerBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_HIGH
import com.google.android.exoplayer2.ui.PlayerNotificationManager as PlayerNotificationManager

class VideoPlayerActivity : AppCompatActivity(), ServiceConnection, Player.Listener{
    private lateinit var binding: ActivityVideoPlayerBinding

    companion object {
       // lateinit var player: ExoPlayer

        lateinit var playerList: ArrayList<VideoDataClass>
        var position:Int = -1

        var videoService: VideoPlayerService?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val intent = Intent(this,VideoPlayerService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)
        startService(intent)
        intializeLayout()





       // startService(Intent(this, VideoPlayerService::class.java))


   /*     val playerNotificationManager: PlayerNotificationManager
        val notificationID = 1234
        val mediaDescriptionAdapter= object: PlayerNotificationManager.MediaDescriptionAdapter{
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return "VideoTitle"
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
                return null
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return "ContextText"
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                return null
            }

        }

        playerNotificationManager = PlayerNotificationManager
            .Builder(this,notificationID,"ds")
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setChannelImportance(IMPORTANCE_HIGH)
            .setNotificationListener(object : PlayerNotificationManager.NotificationListener{

                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    super.onNotificationPosted(notificationId, notification, ongoing)
                }
                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    super.onNotificationCancelled(notificationId, dismissedByUser)
                }
            })
            .build()
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        playerNotificationManager.setPlayer(videoService!!.mediaPlayer )
        playerNotificationManager.setSmallIcon(R.drawable.video_image)
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX)
*/



    }

    private fun intializeLayout() {
        when(intent.getStringExtra("class")){
            "AllVideos"->{
                playerList = ArrayList()
                playerList.addAll(MainActivity.videoList)
            }
            "FolderActivity"->{
                playerList = ArrayList()
                playerList.addAll(FolderActivity.foldervidList)
            }

            "FavFragList"->{
                playerList = ArrayList()
                val snId = intent.getStringExtra("FavVideoID")
                for (i in MainActivity.videoList.indices){
                   if(MainActivity.videoList[i].id == snId){
                       playerList.add(MainActivity.videoList[i])
                   }
                }
            }

        }
       // createPlayer()

        /*val playerNotificationManager: PlayerNotificationManager
        val notificationID = 1234
        val mediaDescriptionAdapter= object: PlayerNotificationManager.MediaDescriptionAdapter{
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return playerList[position].title
            }

            override fun createCurrentContentIntent(player: Player): PendingIntent? {
              return null
            }

            override fun getCurrentContentText(player: Player): CharSequence? {
                return "ContextText"
            }

            override fun getCurrentLargeIcon(
                player: Player,
                callback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                return null
            }

        }

        playerNotificationManager = PlayerNotificationManager
            .Builder(this,notificationID,"ds")
            .setMediaDescriptionAdapter(mediaDescriptionAdapter)
            .setChannelImportance(IMPORTANCE_HIGH)
            .build()
        playerNotificationManager.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        playerNotificationManager.setPlayer(player)
        playerNotificationManager.setSmallIcon(R.drawable.video_image)
        playerNotificationManager.setPriority(NotificationCompat.PRIORITY_MAX)*/


    }


    private fun createPlayer() {
        videoService!!.mediaPlayer = ExoPlayer.Builder(this).build()
        binding.exoPlayerView.player = videoService!!.mediaPlayer
        var mediaItem : MediaItem
        if(playerList.size ==1){
          mediaItem  = MediaItem.fromUri(playerList[0].atrUri)
        }else{
            mediaItem = MediaItem.fromUri(playerList[position].atrUri)
        }

        videoService!!.mediaPlayer!!.setMediaItem(mediaItem)
      //  val size: Int = playerList.size
        if(videoService!!.mediaPlayer!!.hasNextMediaItem()){
            /*for(position in playerList.indices){
                val secondItem = MediaItem.fromUri(playerList[position].atrUri)
                videoService!!.mediaPlayer!!.addMediaItem(secondItem)
            }*/
            videoService!!.mediaPlayer!!.seekToNext()
            val secItem =MediaItem.fromUri(playerList[videoService!!.mediaPlayer!!.nextMediaItemIndex].atrUri)
            videoService!!.mediaPlayer!!.addMediaItem(secItem)
        }


        videoService!!.mediaPlayer!!.prepare()
        videoService!!.mediaPlayer!!.play()


    }

    override fun onDestroy() {
        super.onDestroy()
        videoService!!.mediaPlayer!!.release()
        stopService(Intent(this, VideoPlayerService::class.java))
    }

    override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
        val binder = p1 as VideoPlayerService.MyBinder
        videoService = binder.currentService()
        createPlayer()
        videoService!!.mediaPlayer!!.addListener(this@VideoPlayerActivity)
        videoService!!.showNotification(R.drawable.ic_pause)
    }

    override fun onServiceDisconnected(p0: ComponentName?) {
       videoService = null
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if(isPlaying){
            videoService!!.showNotification(R.drawable.ic_pause)
        }
        if(!isPlaying){
            videoService!!.showNotification(R.drawable.ic_play)
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {

        events.contains(Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED)

        super.onEvents(player, events)
    }
}