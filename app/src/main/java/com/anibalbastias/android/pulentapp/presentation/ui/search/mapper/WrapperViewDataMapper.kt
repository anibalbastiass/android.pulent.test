package com.anibalbastias.android.pulentapp.presentation.ui.search.mapper

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.common.WrapperData
import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.CollectionResultItemData
import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.TrackResultItemData
import com.anibalbastias.android.pulentapp.data.pulent.PulentAPIGSONManager
import com.anibalbastias.android.pulentapp.domain.base.Mapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.WrapperViewData
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-08.
 */

class WrapperViewDataMapper @Inject constructor(
    private val trackResultItemViewDataMapper: TrackResultItemViewDataMapper,
    private val collectionResultItemViewDataMapper: CollectionResultItemViewDataMapper
) : Mapper<WrapperViewData?, WrapperData?> {

    override fun executeMapping(type: WrapperData?): WrapperViewData? {
        return type?.let {
            when (type.wrapperType) {
                PulentAPIGSONManager.TRACK_WRAPPER_TYPE -> {
                    trackResultItemViewDataMapper.executeMapping(type as TrackResultItemData)
                }
                PulentAPIGSONManager.COLLECTION_WRAPPER_TYPE -> {
                    collectionResultItemViewDataMapper.executeMapping(type as CollectionResultItemData)
                }
                else -> null
            }
        }
    }
}