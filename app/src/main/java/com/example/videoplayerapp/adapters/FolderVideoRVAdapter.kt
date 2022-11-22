package com.example.videoplayerapp.adapters

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.videoplayerapp.R
import com.example.videoplayerapp.VideoDataClass
import com.example.videoplayerapp.VideoPlayerActivity
import com.example.videoplayerapp.databinding.VideoViewItemBinding

class FolderVideoRVAdapter (
    private val context: Context,
    private var videoList: ArrayList<VideoDataClass>,
    private val isFolder: Boolean = false
) : RecyclerView.Adapter<FolderVideoRVAdapter.MyHolder>()
{
    class MyHolder(binding: VideoViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.videoName
        val folder = binding.videoLoc
        val duration = binding.videoDur
        val image = binding.videoImage
        val root = binding.root
        val fav = binding.borderedFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(VideoViewItemBinding.inflate(LayoutInflater.from(context),parent,false))    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.title.text = videoList[position].title
        holder.folder.text = videoList[position].folderName
        holder.duration.text = DateUtils.formatElapsedTime(videoList[position].duration/1000)
        Glide.with(context)
            .asBitmap()
            .load(videoList[position].atrUri)
            .apply(RequestOptions().placeholder(R.drawable.video_image).centerCrop())
            .into(holder.image)

        holder.root.setOnClickListener{
            when{
                isFolder->sendIntent(position,"FolderActivity")
                else->sendIntent(position,"AllVideos")
            }
        }
    }

    private fun sendIntent(position: Int, ref: String) {
        VideoPlayerActivity.position = position
        val intent = Intent(context, VideoPlayerActivity::class.java)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context,intent,null)
    }

    override fun getItemCount(): Int {
       return videoList.size
    }
}