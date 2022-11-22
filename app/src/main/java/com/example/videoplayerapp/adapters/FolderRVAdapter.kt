package com.example.videoplayerapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.videoplayerapp.FolderActivity
import com.example.videoplayerapp.FolderDataClass
import com.example.videoplayerapp.VideoDataClass
import com.example.videoplayerapp.databinding.FolderViewItemBinding

class FolderRVAdapter(private val context: Context, private var folderlist: ArrayList<FolderDataClass>) :
RecyclerView.Adapter<FolderRVAdapter.myHolder>(){
    class myHolder(binding: FolderViewItemBinding):RecyclerView.ViewHolder(binding.root) {
        val fname = binding.folderNameFV
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderRVAdapter.myHolder {

        return myHolder(FolderViewItemBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: FolderRVAdapter.myHolder, position: Int) {

        holder.fname.text = folderlist[position].folderName
        holder.fname.setOnClickListener{
            val intent = Intent(context, FolderActivity::class.java)
            intent.putExtra("position", position)
            ContextCompat.startActivity(context,intent,null)
        }
    }

    override fun getItemCount(): Int {

        return folderlist.size
    }
}