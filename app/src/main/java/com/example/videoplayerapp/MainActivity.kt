package com.example.videoplayerapp

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.SurfaceControl
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.example.videoplayerapp.databinding.ActivityMainBinding
import com.example.videoplayerapp.db.LikeEntity
import com.example.videoplayerapp.fragments.FavouritesFragment
import com.example.videoplayerapp.fragments.FoldersFragment
import com.example.videoplayerapp.fragments.VideosFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.io.File
import java.lang.Exception


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var progressBarStatus :Int =0

    lateinit var viewModel: LikedVideoViewModel

    companion object{
        lateinit var videoList : ArrayList<VideoDataClass>
        lateinit var folderList : ArrayList<FolderDataClass>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel = ViewModelProvider(this)[LikedVideoViewModel::class.java]

        if(requestPermission()){

            folderList = ArrayList()
            videoList = ArrayList()
            loadVideosIntoRecyclerView()
            //videoList = getAllVideos()
            setFragment(VideosFragment())


        }
       // requestPermission()


        binding.mainBottomNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.vidBotM-> setFragment(VideosFragment())
                R.id.fileBotM-> setFragment(FoldersFragment())
                R.id.favBotM-> setFragment(FavouritesFragment())
            }
            return@setOnItemSelectedListener true
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return false
    }
    private fun setFragment(fragment: Fragment){

        val transation = supportFragmentManager.beginTransaction()
        transation.replace(R.id.main_frameL,fragment)
        transation.disallowAddToBackStack()
        transation.commit()
    }

    private fun requestPermission(): Boolean{
        if(ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),1)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show()
                //videoList = getAllVideos()
                loadVideosIntoRecyclerView()
                setFragment(VideosFragment())
                folderList = ArrayList()
            }

            else
                ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE),1)
        }
    }

  private fun loadVideosIntoRecyclerView(){
       lifecycleScope.launch(){



                   val videos = withContext(Dispatchers.Main){getAllVideos()}
                   videoList = videos
           if(videos.size!= viewModel.countSize()){
               for(i in videos.indices){
                   val likeEntity = LikeEntity(songId = videos[i].id.toInt(), name = videos[i].title, liked = false)
                   viewModel.insertRecord(likeEntity)
                   Log.d("Added to main-db", "Added to db: "+videos[i].title)
               }
           }



        }
    }

    @SuppressLint("Range")
    private suspend fun getAllVideos() : ArrayList<VideoDataClass>{

            val list = ArrayList<VideoDataClass>()
         withContext(Dispatchers.Main) {

       // delay(1000L)
            val folderlist = ArrayList<String>()

            val projection = arrayOf(
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.BUCKET_ID
            )
            /* val cursor = this.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Video.Media.DATE_ADDED + " DESC"
            )
            if (cursor != null)
                if (cursor.moveToNext())
                    do {
                        val titleC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE))
                        val idC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
                        val folderC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                        val sizeC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
                        val pathC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
                        val durC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
                                .toLong()
                        try {
                            val file = File(pathC)
                            val arturi = Uri.fromFile(file)
                            val video =
                                VideoDataClass(idC, titleC, durC, folderC, sizeC, pathC, arturi)
                            if (file.exists()) list.add(video)
                        } catch (e: Exception) {

                        }
                    } while (cursor.moveToNext())
            cursor?.close()*/
            application.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.Video.Media.DATE_ADDED + " DESC"
            )?.use { cursor ->
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
                val folderColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                val folderidColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
                val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                val duraColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
                Log.i(TAG, "FOUND ${cursor.count}")

                while (cursor.moveToNext()) {
                    val titleC = cursor.getString(titleColumn)
                    val idC = cursor.getString(idColumn)
                    val folderC = cursor.getString(folderColumn)
                    val folderidC = cursor.getString(folderidColumn)
                    val sizeC = cursor.getString(sizeColumn)
                    val pathC = cursor.getString(pathColumn)
                    val durC = cursor.getString(duraColumn).toLong()


                    try {
                        val file = File(pathC)
                        val arturi = Uri.fromFile(file)
                        val video = VideoDataClass(idC, titleC, durC, folderC, sizeC, pathC, arturi)
                        if (file.exists()) list.add(video)

                        if (!folderlist.contains(folderC)) {
                            folderlist.add(folderC)
                            val foldero = FolderDataClass(folderidC, folderC)
                            folderList.add(foldero)
                        }

                    } catch (e: Exception) {

                    }

                    Log.v(TAG, "Added Video List")
                }


            }


            Log.v(TAG, "Found ${list.size} in vidslist")
        }

    return list
    }
}