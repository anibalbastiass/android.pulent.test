package com.anibalbastias.android.pulentapp.presentation.ui.search.mapper

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.TrackResultItemData
import com.anibalbastias.android.pulentapp.domain.base.Mapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.TrackResultItemViewData
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-07.
 */

class TrackResultItemViewDataMapper @Inject constructor() :
    Mapper<TrackResultItemViewData?, TrackResultItemData?> {

    override fun executeMapping(type: TrackResultItemData?): TrackResultItemViewData? {
        return type?.let { item ->
            return TrackResultItemViewData(
                artworkUrl100 = item.artworkUrl100,
                trackTimeMillis = item.trackTimeMillis,
                country = item.country,
                previewUrl = item.previewUrl,
                artistId = item.artistId,
                trackName = item.trackName,
                collectionName = item.collectionName,
                artistViewUrl = item.artistViewUrl,
                discNumber = item.discNumber,
                trackCount = item.trackCount,
                artworkUrl30 = item.artworkUrl30,
                wrapperType = item.wrapperType,
                currency = item.currency,
                collectionId = item.collectionId,
                isStreamable = item.isStreamable,
                trackExplicitness = item.trackExplicitness,
                collectionViewUrl = item.collectionViewUrl,
                trackNumber = item.trackNumber,
                releaseDate = item.releaseDate,
                kind = item.kind,
                trackId = item.trackId,
                collectionPrice = item.collectionPrice,
                discCount = item.discCount,
                primaryGenreName = item.primaryGenreName,
                trackPrice = item.trackPrice,
                collectionExplicitness = item.collectionExplicitness,
                trackViewUrl = item.trackViewUrl,
                artworkUrl60 = item.artworkUrl60,
                trackCensoredName = item.trackCensoredName,
                artistName = item.artistName,
                collectionCensoredName = item.collectionCensoredName
            )
        }
    }
}