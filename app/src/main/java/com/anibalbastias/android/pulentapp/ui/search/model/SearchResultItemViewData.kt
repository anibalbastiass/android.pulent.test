package com.anibalbastias.android.pulentapp.ui.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by anibalbastias on 2019-09-07.
 */

@Parcelize
data class SearchResultItemViewData(
    val artworkUrl100: String? = null,
    val trackTimeMillis: Int? = null,
    val country: String? = null,
    val previewUrl: String? = null,
    val artistId: Int? = null,
    val trackName: String? = null,
    val collectionName: String? = null,
    val artistViewUrl: String? = null,
    val discNumber: Int? = null,
    val trackCount: Int? = null,
    val artworkUrl30: String? = null,
    val wrapperType: String? = null,
    val currency: String? = null,
    val collectionId: Int? = null,
    val isStreamable: Boolean? = null,
    val trackExplicitness: String? = null,
    val collectionViewUrl: String? = null,
    val trackNumber: Int? = null,
    val releaseDate: String? = null,
    val kind: String? = null,
    val trackId: Int? = null,
    val collectionPrice: Double? = null,
    val discCount: Int? = null,
    val primaryGenreName: String? = null,
    val trackPrice: Double? = null,
    val collectionExplicitness: String? = null,
    val trackViewUrl: String? = null,
    val artworkUrl60: String? = null,
    val trackCensoredName: String? = null,
    val artistName: String? = null,
    val collectionCensoredName: String? = null
) : Parcelable