package com.anibalbastias.android.pulentapp.presentation.ui.resultitem

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.transition.TransitionInflater
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.databinding.FragmentResultDetailMainBinding
import com.anibalbastias.android.pulentapp.presentation.appComponent
import com.anibalbastias.android.pulentapp.presentation.getAppContext
import com.anibalbastias.android.pulentapp.presentation.ui.resultitem.interfaces.TrackListener
import com.anibalbastias.android.pulentapp.presentation.ui.resultitem.viewmodel.ResultItemViewModel
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.TrackResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.util.*
import com.anibalbastias.android.pulentapp.presentation.util.media.MediaPlayerUtils
import kotlinx.android.synthetic.main.view_cell_media_player.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 * Created by anibalbastias on 2019-09-08.
 */

class ResultItemFragment : BaseModuleFragment(),
    TrackListener<TrackResultItemViewData> {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_result_detail_main

    private var mediaUtils: MediaPlayerUtils? = null
    private lateinit var binding: FragmentResultDetailMainBinding

    private lateinit var resultItemViewModel: ResultItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        appComponent().inject(this)
        navBaseViewModel = getViewModel(viewModelFactory)
        resultItemViewModel = getViewModel(viewModelFactory)
        sharedViewModel = activity!!.getViewModel(SavedStateViewModelFactory(getAppContext(), this))
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavController(this@ResultItemFragment.view)

        binding = DataBindingUtil.bind<ViewDataBinding>(view) as FragmentResultDetailMainBinding
        binding.sharedViewModel = sharedViewModel
        binding.resultItemViewModel = resultItemViewModel
        binding.trackListener = this
        binding.lifecycleOwner = this

        initViewModel()
        initToolbar()

        resultItemViewModel.apply {
            getSearchResultsLiveData().value?.data?.let {
                setResultsData(it)
            } ?: run {
                isLoading.set(true)
                sharedViewModel.apply {
                    keyword.set("${resultItemViewData.artistName} ${resultItemViewData.collectionName}}")
                }
                getSearchSongsResultsData()
            }
        }
    }

    private fun setResultsData(result: SearchMusicViewData) {
        resultItemViewModel.isLoading.set(false)
        resultItemViewModel.trackListResult.set(
            result.results as? ArrayList<TrackResultItemViewData?>
        )
    }

    override fun onClickView(view: View, item: TrackResultItemViewData) {}

    override fun onPlayPauseTrack(item: TrackResultItemViewData) {
        // Reset all tracks
        resultItemViewModel.trackListResult.get()?.forEach {
            it?.isPlaying?.set(false)
        }

        // Update playable track
        item.isPlaying.set(true)

        setStatusSeekbar(false)

        binding.playerContainer.mediaPlayerDisplayText.text = "${item.artistName} - ${item.trackName}"
        binding.playerContainer.mediaPlayerCounter.text = getString(R.string.initial_time)

        val durationInMilliseconds = item.trackTimeMillis!!

        binding.playerContainer.mediaPlayerTotalTime.text = String.format(
            Locale.getDefault(), MediaPlayerUtils.FORMAT_TIME,
            TimeUnit.MILLISECONDS.toMinutes(durationInMilliseconds.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(durationInMilliseconds.toLong())
        )
        setStatusSeekbar(true)

        mediaUtils?.onDestroy()

        setMediaPlayer(item.previewUrl)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setStatusSeekbar(isEnabled: Boolean) {
        binding.playerContainer.mediaPlayerSeekbar.setOnTouchListener { _, _ -> !isEnabled }
    }

    private fun setMediaPlayer(url: String?) {
        resultItemViewModel.isFirstTimePlayed.set(true)
        mediaUtils = MediaPlayerUtils()

        try {
            mediaUtils?.setupAudioPlayer(context!!,
                url!!,
                resultItemViewModel,
                mediaPlayerCounterSlash,
                mediaPlayerPlayButton,
                mediaPlayerSeekbar,
                mediaPlayerCounter,
                mediaPlayerTotalTime,
                mediaPlayerProgress, object : MediaPlayerUtils.DownloadListener {

                    override fun onSuccessDownloadAudioFile() {
                        if (activity != null) {
                            activity!!.runOnUiThread {
                                showPlayer()

                                try {
                                    if (mediaPlayerPlayButton != null) {
                                        resultItemViewModel.run {

                                            mediaPlayerPlayButton.isEnabled = true

                                            if (isFirstTimePlayed.get()) {
                                                isFirstTimePlayed.set(false)

                                                if (!onPauseButton.get() && seekPosition.get() == 0) {
                                                    mediaPlayerPlayButton.postDelayed({
                                                        mediaPlayerPlayButton.performClick()
                                                    }, 100)
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }

                    override fun onFailedDownloadAudioFile() {
                        activity?.runOnUiThread {
                            dismissLoadingInGetCallRecording()
                            mediaPlayerDisplayText?.setText(R.string.audio_error)
                        }
                    }
                })
        } catch (e: Exception) {
            Log.d("Error set Media Player", e.message)
        }
        activateLoadingAndSeek()
    }

    private fun activateLoadingAndSeek() {
        mediaPlayerPlayButton?.run {
            isEnabled = true
            visible()
        }
        mediaPlayerSeekbar.visible()
        resultItemViewModel?.onPauseButton.set(false)
    }

    private fun dismissLoadingInGetCallRecording() {
        mediaPlayerProgress?.gone()

        mediaPlayerTotalTime?.visible()
        mediaPlayerCounter?.visible()
        mediaPlayerCounterSlash?.visible()

        mediaPlayerPlayButton?.visible()
        mediaPlayerSeekbar?.thumb?.mutate()?.alpha = 0
        listenerContainer.visible()
    }

    private fun showPlayer() {
        mediaPlayerSeekbar?.thumb?.mutate()?.alpha = 255
        mediaPlayerPlayButton?.visible()
        listenerContainer?.visible()
        mediaPlayerProgress?.gone()
        mediaPlayerCounter?.visible()
        mediaPlayerTotalTime?.visible()
        mediaPlayerCounterSlash?.visible()
    }

    private fun initViewModel() {
        implementObserver(resultItemViewModel.getSearchResultsLiveData(),
            successBlock = { viewData -> setResultsData(viewData) },
            loadingBlock = { showLoadingView() },
            errorBlock = { showErrorView(it) })
    }

    private fun showErrorView(message: String?) {
        resultItemViewModel.isLoading.set(false)
    }

    private fun showLoadingView() {
        resultItemViewModel.isLoading.set(true)
    }

    private fun initToolbar() {
        binding.toolbar?.run {
            applyFontForToolbarTitle(activity!!)
            setArrowUpToolbar(activity!!)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaUtils?.onDestroy()
    }

    override fun onPause() {
        Log.d(tagName(), "onPause ")
        resultItemViewModel.onPauseButton.set(true)

        if (mediaPlayerSeekbar != null) {
            resultItemViewModel?.seekPosition.set(mediaUtils?.getCurrentPosition()?.toInt()!!)
            mediaUtils?.onPause()
        }
        super.onPause()
    }
}