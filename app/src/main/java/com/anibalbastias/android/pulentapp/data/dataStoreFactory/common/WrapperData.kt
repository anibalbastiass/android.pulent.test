package com.anibalbastias.android.pulentapp.data.dataStoreFactory.common

import com.google.gson.annotations.SerializedName

/**
 * Created by anibalbastias on 2019-09-08.
 */

open class WrapperData : TypeData() {

    @field:SerializedName("wrapperType")
    open var wrapperType: String? = null

    @field:SerializedName("trackCount")
    open var trackCount: Int? = null

    @field:SerializedName("releaseDate")
    open var releaseDate: String? = null

    @field:SerializedName("primaryGenreName")
    open var primaryGenreName: String? = null

    @field:SerializedName("collectionViewUrl")
    open var collectionViewUrl: String? = null

    @field:SerializedName("collectionPrice")
    open var collectionPrice: Int? = null

    @field:SerializedName("collectionName")
    open var collectionName: String? = null

    @field:SerializedName("collectionId")
    open var collectionId: Int? = null

    @field:SerializedName("collectionCensoredName")
    open var collectionCensoredName: String? = null

    @field:SerializedName("artworkUrl30")
    var artworkUrl30: String? = null

    @field:SerializedName("artworkUrl60")
    open var artworkUrl60: String? = null

    @field:SerializedName("artworkUrl100")
    open var artworkUrl100: String? = null

    @field:SerializedName("artistViewUrl")
    open var artistViewUrl: String? = null

    @field:SerializedName("artistName")
    open var artistName: String? = null

    @field:SerializedName("artistId")
    open var artistId: Int? = null
}