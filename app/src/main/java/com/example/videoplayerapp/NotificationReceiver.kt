package com.example.videoplayerapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationReceiver: BroadcastReceiver() {

    var videoService: VideoPlayerService?=null
    override fun onReceive(p0: Context?, p1: Intent?) {
        when(p1?.action){
            Application.PREV-> Toast.makeText(p0,"Previous", Toast.LENGTH_SHORT).show()
            Application.PLAY-> if(VideoPlayerActivity.videoService!!.mediaPlayer!!.isPlaying)pauseVideo() else playVideo()
            Application.NEXT-> Toast.makeText(p0,"Next", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playVideo(){
        VideoPlayerActivity.videoService!!.mediaPlayer!!.play()
        VideoPlayerActivity.videoService!!.showNotification(R.drawable.ic_pause)

    }

    private fun pauseVideo(){
        VideoPlayerActivity.videoService!!.mediaPlayer!!.pause()
        VideoPlayerActivity.videoService!!.showNotification(R.drawable.ic_play)

    }
}