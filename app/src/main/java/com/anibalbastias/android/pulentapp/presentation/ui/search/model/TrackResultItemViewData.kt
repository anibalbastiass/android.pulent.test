package com.anibalbastias.android.pulentapp.presentation.ui.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by anibalbastias on 2019-09-07.
 */

@Parcelize
data class TrackResultItemViewData(
    override var artworkUrl100: String? = null,
    override var collectionName: String? = null,
    override var artistId: Int? = null,
    override var artistViewUrl: String? = null,
    override var trackCount: Int? = null,
    override var artworkUrl30: String? = null,
    override var wrapperType: String? = null,
    override var collectionId: Int? = null,
    override var collectionViewUrl: String? = null,
    override var releaseDate: String? = null,
    override var collectionPrice: Int? = null,
    override var artworkUrl60: String? = null,
    override var primaryGenreName: String? = null,
    override var artistName: String? = null,
    override var collectionCensoredName: String? = null,
    var trackTimeMillis: Int? = null,
    var country: String? = null,
    var keyword: String? = null,
    var previewUrl: String? = null,
    var trackName: String? = null,
    var discNumber: Int? = null,
    var currency: String? = null,
    var isStreamable: Boolean? = null,
    var trackExplicitness: String? = null,
    var trackNumber: Int? = null,
    var kind: String? = null,
    var trackId: Int? = null,
    var discCount: Int? = null,
    var trackPrice: Double? = null,
    var collectionExplicitness: String? = null,
    var trackViewUrl: String? = null,
    var trackCensoredName: String? = null
) : WrapperViewData(), Parcelable