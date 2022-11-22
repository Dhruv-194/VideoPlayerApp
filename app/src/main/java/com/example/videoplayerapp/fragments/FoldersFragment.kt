package com.example.videoplayerapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoplayerapp.MainActivity
import com.example.videoplayerapp.R
import com.example.videoplayerapp.adapters.FolderRVAdapter
import com.example.videoplayerapp.adapters.VideoRVAdapter
import com.example.videoplayerapp.databinding.FragmentFoldersBinding
import com.example.videoplayerapp.databinding.FragmentVideosBinding

class FoldersFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_folders,container,false)
        val binding = FragmentFoldersBinding.bind(view)
        /*val tempList = ArrayList<String>()
        tempList.add("VIDEO 1")
        tempList.add("VIDEO 1")
        tempList.add("VIDEO 1")*/
        binding.FolderRV.setHasFixedSize(true)
        binding.FolderRV.setItemViewCacheSize(10)
        binding.FolderRV.layoutManager = LinearLayoutManager(requireContext())
        binding.FolderRV.adapter = FolderRVAdapter(requireContext(), MainActivity.folderList)
        return view
    }

}