package com.example.videoplayerapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.videoplayerapp.MainActivity
import com.example.videoplayerapp.R
import com.example.videoplayerapp.VideoDataClass
import com.example.videoplayerapp.VideoPlayerActivity
import com.example.videoplayerapp.databinding.VideoViewItemBinding
import com.example.videoplayerapp.db.LikeEntity

class FavVideoListAdapter(private val context: Context,
                          private var likedVideoList: ArrayList<VideoDataClass>,
                            private var videoList : List<LikeEntity>) :
    RecyclerView.Adapter<FavVideoListAdapter.MyHolder>(){

    class MyHolder(binding: VideoViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.videoName
        val folder = binding.videoLoc
        val duration = binding.videoDur
        val image = binding.videoImage
        val root = binding.root
        val fav = binding.borderedFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(VideoViewItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: MyHolder, @SuppressLint("RecyclerView") position: Int) {

        holder.title.text = likedVideoList[position].title
        holder.folder.text = likedVideoList[position].folderName
        holder.duration.text = DateUtils.formatElapsedTime(likedVideoList[position].duration/1000)
        Glide.with(context)
            .asBitmap()
            .load(likedVideoList[position].atrUri)
            .apply(RequestOptions().placeholder(R.drawable.video_image).centerCrop())
            .into(holder.image)

        if(videoList[position].liked){
            holder.fav.setImageResource(R.drawable.ic_favorite)
        }
      //  val songId = videoList[position].songId
        holder.root.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                VideoPlayerActivity.position = 0
                val intent = Intent(context, VideoPlayerActivity::class.java)
                intent.putExtra("class", "FavFragList")
                intent.putExtra("FavVideoID", likedVideoList[position].id)
                ContextCompat.startActivity(context,intent,null)
            }

        })


    }

    override fun getItemCount(): Int {
        return videoList.size
    }
}