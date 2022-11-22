package com.example.videoplayerapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LikeDAO {
    @Query("SELECT * FROM LikedVideos")
    fun getRecords(): List<LikeEntity>

    @Insert
    fun insertRecord(likeEntity: LikeEntity)

    @Query("SELECT EXISTS(SELECT * FROM LikedVideos WHERE song_id=:songId)")
    fun checkIsFav(songId:Int):Boolean

    @Query("SELECT EXISTS(SELECT * FROM LikedVideos WHERE liked IS 1 AND song_id=:songId)")
    fun isLiked(songId: Int):Boolean

    @Query("UPDATE LikedVideos SET liked ='1' WHERE song_id=:songId")
    fun addLike(songId: Int)

    @Query("SELECT COUNT(song_id) FROM LikedVideos")
    fun countSize():Int

    @Query("UPDATE LikedVideos SET liked ='0' WHERE song_id=:songId")
    fun removeLike(songId: Int)

    @Query("SELECT * FROM LikedVideos WHERE liked IS 1")
    fun getLikedVideos(): List<LikeEntity>
}