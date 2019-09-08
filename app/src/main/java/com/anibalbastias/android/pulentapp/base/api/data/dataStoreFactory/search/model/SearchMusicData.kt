package com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.search.model

import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.common.TypeData
import com.google.gson.annotations.SerializedName

data class SearchMusicData(

	@field:SerializedName("resultCount")
	val resultCount: Int? = null,

	@field:SerializedName("results")
	val results: ArrayList<SearchResultItemData?>? = null
) : TypeData()