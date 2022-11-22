package com.example.videoplayerapp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.videoplayerapp.LikedVideoViewModel
import com.example.videoplayerapp.db.LikeDAO
import com.example.videoplayerapp.db.LikeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun getLikeDB(@ApplicationContext ctx: Context) : LikeDatabase{
        return LikeDatabase.getLikeDB(ctx)
    }

    @Singleton
    @Provides
    fun geDAO(likeDatabase: LikeDatabase) : LikeDAO{
        return likeDatabase.getDAO()
    }

}