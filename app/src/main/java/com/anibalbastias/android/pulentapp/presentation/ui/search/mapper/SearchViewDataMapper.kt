package com.anibalbastias.android.pulentapp.presentation.ui.search.mapper

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.SearchMusicData
import com.anibalbastias.android.pulentapp.domain.base.Mapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchResultItemViewData
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
                results = item.results?.map { searchResultItemViewDataMapper.executeMapping(it) } as ArrayList<SearchResultItemViewData?>?
            )
        }
    }
}