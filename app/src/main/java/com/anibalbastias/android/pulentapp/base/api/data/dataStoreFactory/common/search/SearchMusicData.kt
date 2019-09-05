package com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.common.search

import com.google.gson.annotations.SerializedName

data class SearchMusicData(

	@field:SerializedName("resultCount")
	val resultCount: Int? = null,

	@field:SerializedName("results")
	val results: List<SearchResultItemData?>? = null
)