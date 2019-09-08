package com.anibalbastias.android.pulentapp.presentation.service

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.provider.MediaStore
import com.anibalbastias.android.pulentapp.presentation.context
import com.anibalbastias.android.pulentapp.presentation.service.model.AudioData
import android.os.IBinder
import com.anibalbastias.android.pulentapp.presentation.service.model.MediaPlayerConstants


/**
 * Created by anibalbastias on 2019-09-08.
 */

class AudioManager {

    var serviceBound = false
    var audioList: ArrayList<AudioData>? = null
    var player: MediaPlayerService? = null

    //Binding this Client to the AudioPlayer Service
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as MediaPlayerService.LocalBinder
            player = binder.service
            serviceBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            serviceBound = false
        }
    }


    /**
     * Load audio files using [ContentResolver]
     *
     * If this don't works for you, load the audio files to audioList Array your oun way
     */
    @SuppressLint("Recycle")
    private fun loadAudio() {
        val contentResolver = context?.contentResolver!!

        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val sortOrder = MediaStore.Audio.Media.TITLE + " ASC"

        val cursor = contentResolver.query(uri, null, selection, null, sortOrder)

        if (cursor != null && cursor.count > 0) {
            audioList = ArrayList()
            while (cursor.moveToNext()) {
                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val title =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val album =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val artist =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))

                // Save to audioList
                audioList?.add(AudioData(data, title, album, artist))
            }
        }
        cursor?.close()
    }

    fun Activity.playAudio(audioIndex: Int) {
        //Check is service is active
        if (!serviceBound) {
            //Store Serializable audioList to SharedPreferences
            val storage = StorageUtil(applicationContext)
            audioList?.let { storage.storeAudio(it) }
            storage.storeAudioIndex(audioIndex)

            val playerIntent = Intent(this, MediaPlayerService::class.java)
            startService(playerIntent)
            bindService(playerIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        } else {
            //Store the new audioIndex to SharedPreferences
            val storage = StorageUtil(applicationContext)
            storage.storeAudioIndex(audioIndex)

            //Service is active
            //Send a broadcast to the service -> PLAY_NEW_AUDIO
            val broadcastIntent = Intent(MediaPlayerConstants.BROADCAST_PLAY_NEW_AUDIO)
            sendBroadcast(broadcastIntent)
        }
    }

    fun Activity.onDestroy() {
        if (serviceBound) {
            unbindService(serviceConnection)
            //service is active
            player?.stopSelf()
        }
    }
}