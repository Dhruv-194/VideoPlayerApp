package com.example.videoplayerapp.adapters

import android.content.Context
import android.content.Intent
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.videoplayerapp.LikedVideoViewModel
import com.example.videoplayerapp.R
import com.example.videoplayerapp.VideoDataClass
import com.example.videoplayerapp.VideoPlayerActivity
import com.example.videoplayerapp.databinding.VideoViewItemBinding
import com.example.videoplayerapp.db.LikeEntity
import com.example.videoplayerapp.db.RoomRepository
import com.example.videoplayerapp.fragments.VideosFragment
import dagger.hilt.android.AndroidEntryPoint


class VideoRVAdapter(
    private val context: Context,
    private var videoList: ArrayList<VideoDataClass>,
    private val isFolder: Boolean = false,
    private var videosFragment: VideosFragment,
    val opModel: LikedVideoViewModel
) :
RecyclerView.Adapter<VideoRVAdapter.MyHolder>()
{
    //lateinit var viewModel : LikedVideoViewModel
    //var isLiked : Boolean = false
    //var videosFragment : VideosFragment ?=null
    var vidPos : Int = 0
    class MyHolder(binding: VideoViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.videoName
        val folder = binding.videoLoc
        val duration = binding.videoDur
        val image = binding.videoImage
        val root = binding.root
        val fav = binding.borderedFav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoRVAdapter.MyHolder {
        return MyHolder(VideoViewItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: VideoRVAdapter.MyHolder, position: Int) {

        Log.d("adapter-bind", "BindViewHolderCalled")
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
     //   vidPos = position


       /* if(VideosFragment.isLikedDrawable && videoList[position].title == VideosFragment.videoTitle ){
            holder.fav.setImageResource(R.drawable.ic_favorite)
            Log.d("check-if",opModel.toString())
        }*/
        if (opModel.isLiked(videoList[position].id.toInt())){
            holder.fav.setImageResource(R.drawable.ic_favorite)
            Log.d("check-if",opModel.toString())
        }

       /* if(holder.fav.equals(R.drawable.ic_favorite)){
            holder.fav.setImageResource(R.drawable.ic_favorite)
        }*/
        Log.d("check-opModel",opModel.toString())
        holder.fav.setOnClickListener{
            /*isLiked = true*/

            if(opModel.isLiked(videoList[position].id.toInt())){
                Toast.makeText(context,"Un-Liked!",Toast.LENGTH_SHORT).show()
                opModel.removeLike(videoList[position].id.toInt())
                holder.fav.setImageResource(R.drawable.ic_favorite_border)
            }else if(!opModel.isLiked(videoList[position].id.toInt())){
                Toast.makeText(context, "Liked", Toast.LENGTH_SHORT).show()
                opModel.addLike(videoList[position].id.toInt())
                holder.fav.setImageResource(R.drawable.ic_favorite)
            }
            /*if(opModel.isLiked==false){
                opModel.isLiked = true
                Toast.makeText(context,"Liked!",Toast.LENGTH_SHORT).show()
                holder.fav.setImageResource(R.drawable.ic_favorite)
            }else if(opModel.isLiked==true){
                opModel.isLiked = false
                Toast.makeText(context,"Un-Liked!",Toast.LENGTH_SHORT).show()
                holder.fav.setImageResource(R.drawable.ic_favorite_border)
            }

            videosFragment.likeMe(opModel.isLiked, position)*/

            Log.d("favClicked", "Liked!")

            notifyDataSetChanged()
        }
    }



    private fun sendIntent(position: Int, ref: String) {
        VideoPlayerActivity.position = position
        val intent = Intent(context,VideoPlayerActivity::class.java)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context,intent,null)
    }

    override fun getItemCount(): Int {
        return videoList.size
    }


}