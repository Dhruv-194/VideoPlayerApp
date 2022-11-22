package com.example.videoplayerapp.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName =  "LikedVideos")
class LikeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="song_id") val songId:Int,
    @ColumnInfo(name = "id",) val id: Int =0,
    @ColumnInfo(name="VideoTitle")val name:String,
    @ColumnInfo(name="liked")val liked:Boolean

)