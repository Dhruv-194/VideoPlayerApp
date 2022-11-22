package com.example.videoplayerapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videoplayerapp.adapters.FolderVideoRVAdapter
import com.example.videoplayerapp.adapters.VideoRVAdapter
import com.example.videoplayerapp.databinding.ActivityFolderBinding
import com.example.videoplayerapp.db.LikeEntity
import com.example.videoplayerapp.fragments.VideosFragment
import java.io.File
import java.lang.Exception
import javax.inject.Inject

class FolderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFolderBinding

    companion object{
        lateinit var foldervidList : ArrayList<VideoDataClass>
    }
    /*@Inject
   public var viewModel: LikedVideoViewModel?=null
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFolderBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

     /*   viewModel?.getRecordObserver()?.observe(this,object :
            Observer<List<LikeEntity>> {
            override fun onChanged(t: List<LikeEntity>?) {}})*/
       /* var viewModel: LikedVideoViewModel = ViewModelProvider(this)[LikedVideoViewModel::class.java]

        viewModel = ViewModelProvider(this)[LikedVideoViewModel::class.java]*/

        val position = intent.getIntExtra("position",0)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = MainActivity.folderList[position].folderName

        foldervidList = getAllVideos(MainActivity.folderList[position].id)

        binding.FolderVideoRV.setHasFixedSize(true)
        binding.FolderVideoRV.setItemViewCacheSize(10)
        binding.FolderVideoRV.layoutManager = LinearLayoutManager(this@FolderActivity)
        binding.FolderVideoRV.adapter = FolderVideoRVAdapter(this@FolderActivity, foldervidList, true)
    /*viewModel?.let {
            VideoRVAdapter(
                this@FolderActivity, foldervidList, true,
                VideosFragment(),
                it
            )
        }*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
            return true
        }
        return false
    }

    @SuppressLint("Range")
    private  fun getAllVideos(folderId : String) : ArrayList<VideoDataClass>{

        val list = ArrayList<VideoDataClass>()
        //val folderlist = ArrayList<String>()

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

        val selection = MediaStore.Video.Media.BUCKET_ID + " like? "
        val selectionArgs = arrayOf(folderId)

        application.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            MediaStore.Video.Media.DATE_ADDED + " DESC"
        )?.use { cursor ->
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val folderColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
           // val folderidColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
            val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val duraColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
            Log.i(ContentValues.TAG, "FOUND ${cursor.count}")

            while (cursor.moveToNext()) {
                val titleC = cursor.getString(titleColumn)
                val idC = cursor.getString(idColumn)
                val folderC = cursor.getString(folderColumn)
             //   val folderidC = cursor.getString(folderidColumn)
                val sizeC = cursor.getString(sizeColumn)
                val pathC = cursor.getString(pathColumn)
                val durC = cursor.getString(duraColumn).toLong()


                try {
                    val file = File(pathC)
                    val arturi = Uri.fromFile(file)
                    val video = VideoDataClass(idC, titleC, durC, folderC, sizeC, pathC, arturi)
                    if (file.exists()) list.add(video)

                  /*  if(!folderlist.contains(folderC)){
                        folderlist.add(folderC)
                        val foldero = FolderDataClass(folderidC,folderC)
                        MainActivity.folderList.add(foldero)
                    }*/

                } catch (e: Exception) {

                }

                Log.v(ContentValues.TAG, "Added Video")
            }


        }


        Log.v(ContentValues.TAG, "Found ${list.size} vids")


        return list
    }
}