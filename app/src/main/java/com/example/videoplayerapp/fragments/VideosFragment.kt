package com.example.videoplayerapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoplayerapp.LikedVideoViewModel
import com.example.videoplayerapp.MainActivity
import com.example.videoplayerapp.MainActivity.Companion.videoList
import com.example.videoplayerapp.R
import com.example.videoplayerapp.adapters.VideoRVAdapter
import com.example.videoplayerapp.adapters.VideoRVAdapter.MyHolder
import com.example.videoplayerapp.databinding.FragmentVideosBinding
import com.example.videoplayerapp.db.LikeEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.Delegates

@AndroidEntryPoint
class VideosFragment : Fragment() {

 lateinit var viewModel: LikedVideoViewModel
    var videoRVAdapter: VideoRVAdapter?=null

companion object{
    var isLikedDrawable:Boolean = false
    lateinit var videoTitle : String
    lateinit var binding: FragmentVideosBinding;
}

    lateinit var listTitle : ArrayList<String>

    var videoPos by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_videos,container,false)
        binding = FragmentVideosBinding.bind(view)
        val tempList = ArrayList<String>()
        tempList.add("VIDEO 1")
        tempList.add("VIDEO 1")
        tempList.add("VIDEO 1")


        listTitle = ArrayList()
        listTitle.add("chamber_valo")
        listTitle.add("videoplayback")
        initVM()

        binding.VideoRV.setHasFixedSize(true)
        binding.VideoRV.setItemViewCacheSize(10)
        binding.VideoRV.layoutManager = LinearLayoutManager(requireContext())
        binding.VideoRV.adapter = VideoRVAdapter(
            requireContext(),
            MainActivity.videoList,
            false,
            this,
            viewModel
        )

        //initVM()

        /*if(videoRVAdapter!!.isLiked){
            val likeEntity = LikeEntity(name = videoList[position].title.toString())
            viewModel.insertRecord(likeEntity)
            Toast.makeText(context,"Added to db", Toast.LENGTH_SHORT).show()
        }*/

        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        /*listTitle = ArrayList()
        listTitle.add("chamber_valo")
        listTitle.add("videoplayback")
        initVM()*/
    }

    private fun initVM(){

        viewModel = ViewModelProvider(this)[LikedVideoViewModel::class.java]
        viewModel.getRecordObserver().observe(viewLifecycleOwner,object : Observer<List<LikeEntity>>{
            override fun onChanged(t: List<LikeEntity>?) {
                //   Toast.makeText(context,"added to vm", Toast.LENGTH_SHORT).show()
                /*var i = 0
                t?.forEach {
                    if(it.name== listTitle[i].toString()) {
                        isLikedDrawable = true
                        videoTitle = it.name
                        Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
                        Log.d("viewmodel check", listTitle.toString())
                        videoRVAdapter

                    }
                }
                i++*/


            }

        })
    }

    /*fun likeMe( isLike : Boolean, pos: Int){
        if(isLike){

            viewModel.addLike(videoList[pos].id.toInt())
*//*            val likeEntity = LikeEntity(name = videoList[pos].title.toString(), songId = videoList[pos].id.toInt())
            viewModel.insertRecord(likeEntity)
            listTitle.add(videoList[pos].title)*//*
            Toast.makeText(context,"Updated to like", Toast.LENGTH_SHORT).show()
            Log.d("Updated to like", "Updated to like"+ videoList[pos].title)
           // likePos(pos)
        }
        else if(!isLike){
            viewModel.removeLike(videoList[pos].id.toInt())
            Toast.makeText(context, "Removed from liked", Toast.LENGTH_SHORT).show()
            Log.d("removed from liked", "Removed from liked"+ videoList[pos].title)
        }
    }*/
   /* fun isLikedDrawable(like : Boolean) : Boolean {
        return like
    }*/

   /* fun likePos(pos: Int){
        videoPos = pos
    }*/

}

