package com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.realm

import com.anibalbastias.android.pulentapp.domain.base.Mapper
import com.anibalbastias.android.pulentapp.domain.search.model.ResultItemRealmData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.CollectionResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.TrackResultItemViewData
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-07.
 */

open class SearchResultItemRealmMapper @Inject constructor() :
    Mapper<ResultItemRealmData?, CollectionResultItemViewData?> {

    lateinit var keywordValue: String

    override fun executeMapping(type: CollectionResultItemViewData?): ResultItemRealmData? {
        return type?.let { item ->
            val resultItemData = ResultItemRealmData()

            resultItemData.apply {
                keyword = keywordValue
                artworkUrl100 = item.artworkUrl100
                country = item.country
                artistId = item.artistId
                collectionName = item.collectionName
                artistViewUrl = item.artistViewUrl
                trackCount = item.trackCount
                artworkUrl30 = item.artworkUrl30
                wrapperType = item.wrapperType
                currency = item.currency
                collectionId = item.collectionId
                collectionViewUrl = item.collectionViewUrl
                releaseDate = item.releaseDate
                collectionPrice = item.collectionPrice
                primaryGenreName = item.primaryGenreName
                collectionExplicitness = item.collectionExplicitness
                artworkUrl60 = item.artworkUrl60
                artistName = item.artistName
                collectionCensoredName = item.collectionCensoredName
            }
            return resultItemData
        }
    }

    fun inverseExecuteMapping(type: ResultItemRealmData?): CollectionResultItemViewData? {
        return type?.let { item ->
            val resultItemData = CollectionResultItemViewData()

            resultItemData.apply {
                artworkUrl100 = item.artworkUrl100
                country = item.country
                artistId = item.artistId
                collectionName = item.collectionName
                artistViewUrl = item.artistViewUrl
                trackCount = item.trackCount
                artworkUrl30 = item.artworkUrl30
                wrapperType = item.wrapperType
                currency = item.currency
                collectionId = item.collectionId
                collectionViewUrl = item.collectionViewUrl
                releaseDate = item.releaseDate
                collectionPrice = item.collectionPrice
                primaryGenreName = item.primaryGenreName
                collectionExplicitness = item.collectionExplicitness
                artworkUrl60 = item.artworkUrl60
                artistName = item.artistName
                collectionCensoredName = item.collectionCensoredName
            }
            return resultItemData
        }
    }
}