package com.anibalbastias.android.pulentapp.presentation.util.media

/**
 * Created by anibalbastias on 2019-09-08.
 */

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.presentation.ui.resultitem.viewmodel.ResultItemViewModel
import com.anibalbastias.android.pulentapp.presentation.util.gone
import com.anibalbastias.android.pulentapp.presentation.util.visible
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class MediaPlayerUtils : MediaPlayer.OnPreparedListener,
    SeekBar.OnSeekBarChangeListener,
    View.OnClickListener,
    MediaPlayer.OnCompletionListener {

    companion object {
        const val FORMAT_TIME = "%02d:%02d"
        private const val TIME_REFRESH_LOAD_AUDIO: Long = 50
    }

    private var isPlayed = false
    private var isReadyToPlay = false
    private var isVolumeControllerVisible = false
    var mediaPlayer: MediaPlayer? = null
    private var mStartTime = 0.0
    private var mFinalTime = 0.0
    private var mIsDestroy = false
    private var myHandler: Handler? = null
    private var mediaPlayerSeekBar: SeekBar? = null
    private var mediaPlayerCounter: TextView? = null
    private var mediaPlayerPlayButton: ImageView? = null
    private var mediaPlayerVolumeController: SeekBar? = null
    private var mediaPlayerTotalTime: TextView? = null
    private var mFile: File? = null
    private var mContext: Context? = null
    private var mSeekPosition: Int? = 0

    private var liveDataSeekPosition: ResultItemViewModel? = null

    private val mUpdateSongTime = object : Runnable {
        override fun run() {
            if (!mIsDestroy) {
                try {
                    if (mediaPlayer?.isPlaying!!) {
                        mStartTime = mediaPlayer?.currentPosition?.toDouble() ?: 0.0
                        mediaPlayerCounter?.text = String.format(
                            Locale.getDefault(), FORMAT_TIME,
                            TimeUnit.MILLISECONDS.toMinutes(mStartTime.toLong()),
                            TimeUnit.MILLISECONDS.toSeconds(mStartTime.toLong()) -
                                    TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(
                                            mStartTime.toLong()
                                        )
                                    )
                        )
                        mediaPlayerSeekBar?.progress = mStartTime.toInt()

                        myHandler?.postDelayed(this, 100)
                    } else {

                        mStartTime = mediaPlayer?.currentPosition?.toDouble() ?: 0.0
                        if (mStartTime == mFinalTime)
                            resetData()
                    }

                } catch (e: IllegalStateException) {
                    mStartTime = 0.0
                    mFinalTime = 0.0
                }

            } else {
                mStartTime = 0.0
                mFinalTime = 0.0
            }
        }
    }

    @Throws(Exception::class)
    fun setupAudioPlayer(
        context: Context,
        audio_url: String,
        liveDataSeekPosition: ResultItemViewModel,
        mediaPlayerCounterSlash: TextView,
        mediaPlayerPlayButton: ImageView,
        mediaPlayerSeekbar: SeekBar,
        mediaPlayerCounter: TextView,
        mediaPlayerTotalTime: TextView,
        mediaPlayerProgress: ProgressBar, callback: DownloadListener
    ) {
        // Set global vars
        isReadyToPlay = false
        this.mediaPlayerSeekBar = mediaPlayerSeekbar
        this.mediaPlayerCounter = mediaPlayerCounter
        this.mediaPlayerPlayButton = mediaPlayerPlayButton
        this.mediaPlayerVolumeController = mediaPlayerVolumeController
        this.mediaPlayerTotalTime = mediaPlayerTotalTime
        this.mContext = context
        this.mSeekPosition = liveDataSeekPosition.seekPosition.get()
        this.liveDataSeekPosition = liveDataSeekPosition

        // Set Data for media player
        resetData()

        // Set Visibility
        mediaPlayerProgress.visible()
        mediaPlayerTotalTime.gone()
        mediaPlayerCounter.gone()
        mediaPlayerCounterSlash.gone()

        val audioId = "pulent_${Date().time}"

        FileUtils.downloadFileAsync(context, audioId, audio_url,
            object : FileUtils.DownloadFileListener {
                override fun onSuccessDownloadFile(file: File) {
                    Log.e("File", audioId)

                    this@MediaPlayerUtils.mFile = file

                    // Set Media Player
                    try {
                        setAudioMediaPlayer()
                        isReadyToPlay = true

                        callback.onSuccessDownloadAudioFile()
                    } catch (e: IOException) {
                        callback.onFailedDownloadAudioFile()
                        e.printStackTrace()
                    }

                }

                override fun onFailedDownloadFile() {
                    callback.onFailedDownloadAudioFile()
                }
            })


        // Set Visibility buttons
        if (isPlayed)
            mediaPlayerPlayButton.setImageResource(R.drawable.ic_pause)
        else
            mediaPlayerPlayButton.setImageResource(R.drawable.ic_play)

        if (isVolumeControllerVisible)
            mediaPlayerVolumeController?.visible()
        else
            mediaPlayerVolumeController?.gone()

        // set Action Buttons

        // Play Button
        mediaPlayerPlayButton.setOnClickListener(this)

        // Seekbar change listener
        mediaPlayerSeekbar.setOnSeekBarChangeListener(this)
    }

    private fun resetData() {
        mStartTime = 0.0
        mFinalTime = 0.0
        mIsDestroy = false
        isPlayed = false
        isReadyToPlay = true

        mediaPlayerSeekBar?.progress = 0
        mediaPlayerPlayButton?.setImageResource(R.drawable.ic_play)

        myHandler = Handler()
    }

    @Throws(IOException::class)
    fun setAudioMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(mFile?.path)
            mediaPlayer?.isLooping = false
            mediaPlayer?.prepare()
            mediaPlayer?.setOnPreparedListener(this@MediaPlayerUtils)
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer?.setOnCompletionListener(this@MediaPlayerUtils)
        } catch (e: IllegalStateException) {

        }

    }


    override fun onPrepared(mp: MediaPlayer?) {
        mediaPlayer = mp
        try {
            mediaPlayer?.start()

            Handler().postDelayed({
                try {
                    mediaPlayer?.pause()
                } catch (e1: IllegalStateException) {
                    e1.printStackTrace()
                }
            }, 100)

            setDataPlaying()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: KotlinNullPointerException) {
            e.printStackTrace()
        }

    }

    fun startPlaying() {
        mediaPlayer?.start()
    }

    private fun setDataPlaying() {
        try {
            mFinalTime = mediaPlayer?.duration?.toDouble() ?: 0.0
            mStartTime = mediaPlayer?.currentPosition?.toDouble() ?: 0.0

            mediaPlayerSeekBar?.max = mFinalTime.toInt()

            setPlayerCounter()

            val currentPosition = if (liveDataSeekPosition?.seekPosition?.get() != 0)
                liveDataSeekPosition?.seekPosition?.get()
            else
                mStartTime.toInt()


            mediaPlayerSeekBar?.progress = currentPosition!!

            mediaPlayer?.seekTo(liveDataSeekPosition?.seekPosition?.get()!!)

            if (myHandler != null) {
                myHandler?.removeCallbacks(mUpdateSongTime)

                if (mFinalTime != mStartTime)
                    myHandler?.postDelayed(mUpdateSongTime, TIME_REFRESH_LOAD_AUDIO)
                else
                    resetData()
            }
        } catch (e: IllegalStateException) {

        } catch (e: KotlinNullPointerException) {

        }

    }

    private fun setPlayerCounter() {
        mediaPlayerTotalTime?.text = String.format(
            Locale.getDefault(), FORMAT_TIME,
            TimeUnit.MILLISECONDS.toMinutes(mFinalTime.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(mFinalTime.toLong())
        )
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        try {
            if (mediaPlayer != null && fromUser) {
                mediaPlayer?.seekTo(progress)
                mStartTime = mediaPlayer?.currentPosition?.toDouble() ?: 0.0
                seekBar?.progress = progress
                liveDataSeekPosition?.seekPosition?.set(mStartTime.toInt())

                mSeekPosition = mStartTime.toInt()

                mediaPlayerCounter?.text = String.format(
                    Locale.getDefault(), FORMAT_TIME,
                    TimeUnit.MILLISECONDS.toMinutes(mStartTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(mStartTime.toLong()) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(mStartTime.toLong()))
                )
                myHandler?.postDelayed(mUpdateSongTime, TIME_REFRESH_LOAD_AUDIO)
            }
        } catch (e: IllegalStateException) {

        }

    }


    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.mediaPlayerPlayButton ->
                // Play button
                if (isReadyToPlay) {
                    if (isPlayed) {
                        mediaPlayerPlayButton?.setImageResource(R.drawable.ic_play)
                        isPlayed = false
                        pauseMedia()
                    } else {
                        mediaPlayerPlayButton?.setImageResource(R.drawable.ic_pause)
                        isPlayed = true
                        playMedia()
                    }
                }
        }
    }

    fun pauseMedia() {
        try {
            isReadyToPlay = true
            mediaPlayer?.pause()
            mediaPlayerPlayButton?.setImageResource(R.drawable.ic_play)
            liveDataSeekPosition?.seekPosition?.set(mediaPlayer?.currentPosition!!)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: KotlinNullPointerException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun playMedia() {
        try {
            isReadyToPlay = true
            mediaPlayerPlayButton?.setImageResource(R.drawable.ic_pause)

            mediaPlayer?.start()
            setDataPlaying()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: KotlinNullPointerException) {
            e.printStackTrace()
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        try {
            liveDataSeekPosition?.seekPosition?.set(0)
            mSeekPosition = 0
            if (!mp?.isPlaying!!) {
                mediaPlayerPlayButton?.setImageResource(R.drawable.ic_play)
                isPlayed = false

                mp.stop()
                mp.release()

                try {
                    setAudioMediaPlayer()
                    mediaPlayer?.start()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

            }

        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: KotlinNullPointerException) {
            e.printStackTrace()
        }
    }

    fun onDestroy() {
        try {
            mediaPlayer?.release()
            mIsDestroy = true
        } catch (e: IllegalStateException) {

        }

    }

    fun onPause() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (e: IllegalStateException) {

        }

    }

    interface DownloadListener {
        fun onSuccessDownloadAudioFile()

        fun onFailedDownloadAudioFile()
    }

    fun getCurrentPosition() = mStartTime
}