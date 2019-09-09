package com.anibalbastias.android.pulentapp.presentation.ui.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CollectionResultItemViewData(
	
	override var artworkUrl100: String? = null,
	override var collectionViewUrl: String? = null,
	override var releaseDate: String? = null,
	override var artistId: Int? = null,
	override var collectionPrice: Int? = null,
	override var primaryGenreName: String? = null,
	override var collectionName: String? = null,
	override var artistViewUrl: String? = null,
	override var trackCount: Int? = null,
	override var artworkUrl60: String? = null,
	override var artistName: String? = null,
	override var wrapperType: String? = null,
	override var collectionId: Int? = null,
	override var collectionCensoredName: String? = null,

	var country: String? = null,
	var copyright: String? = null,
	var amgArtistId: Int? = null,
	var collectionType: String? = null,
	var collectionExplicitness: String? = null,
	var currency: String? = null
) : WrapperViewData(), Parcelable