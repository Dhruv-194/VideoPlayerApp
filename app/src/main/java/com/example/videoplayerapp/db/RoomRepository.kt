package com.example.videoplayerapp.db

import androidx.room.Dao
import javax.inject.Inject

class RoomRepository @Inject constructor(private val likeDAO: LikeDAO) {

    fun getRecords(): List<LikeEntity>{
        return likeDAO.getRecords()
    }

    fun insertRecords(likeEntity: LikeEntity){
        likeDAO.insertRecord(likeEntity)
    }
    fun checkSongFavourite(songId:Int):Boolean{
       return likeDAO.checkIsFav(songId)
    }

    fun isLiked(songId: Int): Boolean{
        return likeDAO.isLiked(songId)
    }

    fun addLike(songId: Int){
        likeDAO.addLike(songId)
    }

    fun countSize() : Int{
      return  likeDAO.countSize()
    }

    fun removeLiked(songId: Int){
        likeDAO.removeLike(songId)
    }

    fun getLikedVideos() : List<LikeEntity>{
        return likeDAO.getLikedVideos()
    }
}