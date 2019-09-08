package com.anibalbastias.android.pulentapp.ui.search.mapper

import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.search.model.SearchMusicData
import com.anibalbastias.android.pulentapp.base.api.domain.base.Mapper
import com.anibalbastias.android.pulentapp.ui.search.model.SearchMusicViewData
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-07.
 */

class SearchViewDataMapper @Inject constructor(
    private val searchResultItemViewDataMapper: SearchResultItemViewDataMapper
) : Mapper<SearchMusicViewData?, SearchMusicData?> {

    override fun executeMapping(type: SearchMusicData?): SearchMusicViewData? {
        return type?.let {  item ->
            SearchMusicViewData(
                resultCount = item.resultCount,
                results = item.results?.map { searchResultItemViewDataMapper.executeMapping(it) }
            )
        }
    }
}