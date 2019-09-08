package com.anibalbastias.android.pulentapp.presentation.ui.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by anibalbastias on 2019-09-07.
 */

@Parcelize
data class SearchResultItemViewData(
    var artworkUrl100: String? = null,
    var trackTimeMillis: Int? = null,
    var country: String? = null,

    var keyword: String? = null,

    var previewUrl: String? = null,
    var artistId: Int? = null,
    var trackName: String? = null,
    var collectionName: String? = null,
    var artistViewUrl: String? = null,
    var discNumber: Int? = null,
    var trackCount: Int? = null,
    var artworkUrl30: String? = null,
    var wrapperType: String? = null,
    var currency: String? = null,
    var collectionId: Int? = null,
    var isStreamable: Boolean? = null,
    var trackExplicitness: String? = null,
    var collectionViewUrl: String? = null,
    var trackNumber: Int? = null,
    var releaseDate: String? = null,
    var kind: String? = null,
    var trackId: Int? = null,
    var collectionPrice: Double? = null,
    var discCount: Int? = null,
    var primaryGenreName: String? = null,
    var trackPrice: Double? = null,
    var collectionExplicitness: String? = null,
    var trackViewUrl: String? = null,
    var artworkUrl60: String? = null,
    var trackCensoredName: String? = null,
    var artistName: String? = null,
    var collectionCensoredName: String? = null
) : Parcelable