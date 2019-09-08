package com.anibalbastias.android.pulentapp.presentation.ui.search.mapper

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.CollectionResultItemData
import com.anibalbastias.android.pulentapp.domain.base.Mapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.CollectionResultItemViewData
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-07.
 */

class CollectionResultItemViewDataMapper @Inject constructor() :
    Mapper<CollectionResultItemViewData?, CollectionResultItemData?> {

    override fun executeMapping(type: CollectionResultItemData?): CollectionResultItemViewData? {
        return type?.let { item ->
            return CollectionResultItemViewData(
                artworkUrl100 = type.artworkUrl100,
                collectionViewUrl = type.collectionViewUrl,
                releaseDate = type.releaseDate,
                artistId = type.amgArtistId,
                collectionPrice = type.collectionPrice,
                primaryGenreName = type.primaryGenreName,
                collectionName = type.collectionName,
                artistViewUrl = type.artistViewUrl,
                trackCount = type.trackCount,
                artworkUrl60 = type.artworkUrl60,
                artistName = type.artistName,
                wrapperType = type.wrapperType,
                collectionId = type.collectionId,
                collectionCensoredName = type.collectionCensoredName,
                country = type.country,
                copyright = type.copyright,
                amgArtistId = type.amgArtistId,
                collectionType = type.collectionType,
                collectionExplicitness = type.collectionExplicitness,
                currency = type.currency
            )
        }
    }
}