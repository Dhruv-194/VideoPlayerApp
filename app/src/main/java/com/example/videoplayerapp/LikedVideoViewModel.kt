package com.example.videoplayerapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.videoplayerapp.db.LikeEntity
import com.example.videoplayerapp.db.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LikedVideoViewModel @Inject  constructor(private val repository: RoomRepository): ViewModel() {

    var likeData: MutableLiveData<List<LikeEntity>> = MutableLiveData()
    var favLikeData: MutableLiveData<List<LikeEntity>> = MutableLiveData()

    companion object{
    }
    init {
        loadRecord()
    }
    var isLiked : Boolean = false
    fun getRecordObserver(): MutableLiveData<List<LikeEntity>>{
        return likeData
    }

    fun getFavVideoObserver() : MutableLiveData<List<LikeEntity>>{
        return favLikeData
    }


    fun loadRecord(){
        val list = repository.getRecords()

        likeData.postValue(list)
    }
    fun checkIsFavourite(songId:Int):Boolean{
       return repository.checkSongFavourite(songId)
    }

    fun insertRecord(likeEntity: LikeEntity){
        repository.insertRecords(likeEntity)
        loadRecord()
    }

    fun isLiked(songId: Int): Boolean{
        return repository.isLiked(songId)
    }

    fun addLike(songId: Int){
        repository.addLike(songId)
        loadRecord()
    }

    fun countSize():Int{
        return repository.countSize()
    }

    fun removeLike(songId: Int){
        repository.removeLiked(songId)
        loadRecord()
    }

    fun loadLikedVideos() : List<LikeEntity>{
        val likedList = repository.getLikedVideos()
        favLikeData.postValue(likedList)
        return likedList
    }
}