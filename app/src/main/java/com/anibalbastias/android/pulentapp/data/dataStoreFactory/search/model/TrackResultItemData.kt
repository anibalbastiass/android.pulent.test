package com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.common.WrapperData
import com.google.gson.annotations.SerializedName

data class TrackResultItemData(

	@field:SerializedName("trackTimeMillis")
	val trackTimeMillis: Int? = null,

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("previewUrl")
	val previewUrl: String? = null,

	@field:SerializedName("trackName")
	val trackName: String? = null,

	@field:SerializedName("discNumber")
	val discNumber: Int? = null,

	@field:SerializedName("currency")
	val currency: String? = null,

	@field:SerializedName("isStreamable")
	val isStreamable: Boolean? = null,

	@field:SerializedName("trackExplicitness")
	val trackExplicitness: String? = null,

	@field:SerializedName("trackNumber")
	val trackNumber: Int? = null,

	@field:SerializedName("kind")
	val kind: String? = null,

	@field:SerializedName("trackId")
	val trackId: Int? = null,

	@field:SerializedName("discCount")
	val discCount: Int? = null,

	@field:SerializedName("trackPrice")
	val trackPrice: Double? = null,

	@field:SerializedName("collectionExplicitness")
	val collectionExplicitness: String? = null,

	@field:SerializedName("trackViewUrl")
	val trackViewUrl: String? = null,

	@field:SerializedName("trackCensoredName")
	val trackCensoredName: String? = null

) : WrapperData()