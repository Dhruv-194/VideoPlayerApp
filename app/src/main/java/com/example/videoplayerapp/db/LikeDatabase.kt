package com.example.videoplayerapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LikeEntity::class], version = 1)
abstract class LikeDatabase : RoomDatabase(){

   abstract fun getDAO(): LikeDAO
    companion object{
        private var dbINSTANCE : LikeDatabase?=null

        fun getLikeDB(context: Context): LikeDatabase{
            if(dbINSTANCE==null){
                dbINSTANCE = Room.databaseBuilder<LikeDatabase>(
                    context.applicationContext, LikeDatabase::class.java, "LikedDB"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return dbINSTANCE!!
        }
    }

}