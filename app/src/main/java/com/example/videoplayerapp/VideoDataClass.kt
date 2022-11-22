package com.example.videoplayerapp

import android.net.Uri
import java.time.Duration

data class VideoDataClass(
    val id: String,
    val title: String,
    val duration: Long =0,
    val folderName: String,
    val size: String,
    val path: String,
    val atrUri: Uri
)

data class FolderDataClass(
    val id: String,
    val folderName: String
)
