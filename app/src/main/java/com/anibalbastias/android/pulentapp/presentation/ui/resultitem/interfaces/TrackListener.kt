package com.anibalbastias.android.pulentapp.presentation.ui.resultitem.interfaces

import android.view.View
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.TrackResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.util.adapter.base.BaseBindClickHandler

/**
 * Created by anibalbastias on 2019-08-13.
 */

interface TrackListener<T> : BaseBindClickHandler<T> {

    override fun onClickView(view: View, item: T)
    fun onPlayPauseTrack(item: TrackResultItemViewData)
}