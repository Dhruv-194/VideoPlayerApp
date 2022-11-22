package com.example.videoplayerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoplayerapp.LikedVideoViewModel
import com.example.videoplayerapp.MainActivity
import com.example.videoplayerapp.R
import com.example.videoplayerapp.VideoDataClass
import com.example.videoplayerapp.adapters.FavVideoListAdapter
import com.example.videoplayerapp.databinding.FragmentFavouritesBinding
import com.example.videoplayerapp.databinding.FragmentVideosBinding
import com.example.videoplayerapp.db.LikeEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    lateinit var viewModel: LikedVideoViewModel
    lateinit var binding: FragmentFavouritesBinding;
    lateinit var favVideoList : ArrayList<LikeEntity>
    lateinit var likedVideoList : ArrayList<VideoDataClass>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view  =  inflater.inflate(R.layout.fragment_favourites, container, false)
        binding = FragmentFavouritesBinding.bind(view)

        initVM()

        binding.FavVideoRV.setHasFixedSize(true)
        binding.FavVideoRV.setItemViewCacheSize(10)
        binding.FavVideoRV.layoutManager = LinearLayoutManager(requireContext())
        binding.FavVideoRV.adapter = FavVideoListAdapter(
            requireContext(),
            likedVideoList,
            favVideoList
        )

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initVM() {
        viewModel = ViewModelProvider(this)[LikedVideoViewModel::class.java]

        viewModel.getFavVideoObserver().observe(viewLifecycleOwner, object : Observer<List<LikeEntity>>{
            override fun onChanged(t: List<LikeEntity>?) {

            }

        })
        favVideoList = ArrayList()
        favVideoList.addAll(viewModel.loadLikedVideos())
        likedVideoList = ArrayList()

        for(j in favVideoList.indices) {
            for (i in MainActivity.videoList.indices) {
                if (favVideoList[j].songId.toString() == MainActivity.videoList[i].id) {
                    likedVideoList.add(MainActivity.videoList[i])
                }
            }
        }
    }
}