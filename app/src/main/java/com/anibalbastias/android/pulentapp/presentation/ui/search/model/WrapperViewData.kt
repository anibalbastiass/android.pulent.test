package com.anibalbastias.android.pulentapp.presentation.ui.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by anibalbastias on 2019-09-08.
 */

@Parcelize
open class WrapperViewData : Parcelable {
    open var wrapperType: String? = null
    open var trackCount: Int? = null
    open var releaseDate: String? = null
    open var primaryGenreName: String? = null
    open var collectionViewUrl: String? = null
    open var collectionPrice: Int? = null
    open var collectionName: String? = null
    open var collectionId: Int? = null
    open var collectionCensoredName: String? = null
    open var artworkUrl30: String? = null
    open var artworkUrl60: String? = null
    open var artworkUrl100: String? = null
    open var artistViewUrl: String? = null
    open var artistName: String? = null
    open var artistId: Int? = null
}