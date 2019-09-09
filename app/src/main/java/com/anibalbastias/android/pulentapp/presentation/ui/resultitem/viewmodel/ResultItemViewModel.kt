package com.anibalbastias.android.pulentapp.presentation.ui.resultitem.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.base.subscriber.BaseSubscriber
import com.anibalbastias.android.pulentapp.base.view.BaseViewModel
import com.anibalbastias.android.pulentapp.base.view.Resource
import com.anibalbastias.android.pulentapp.base.view.ResourceState
import com.anibalbastias.android.pulentapp.domain.search.usecase.GetSearchMusicUseCase
import com.anibalbastias.android.pulentapp.presentation.context
import com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.SearchViewDataMapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.TrackResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.util.empty
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-08.
 */

class ResultItemViewModel @Inject constructor(
    private val getSearchMusicUseCase: GetSearchMusicUseCase,
    private val searchViewDataMapper: SearchViewDataMapper
) : BaseViewModel() {

    companion object {
        const val PAGE_SIZE = 100
        private const val URL_FORMAT =
            "%s&offset=%d&mediaType=%s&limit=%d&country=%s&entity=%s&sort=recent"
        private const val SEARCH_URL = "search?term="

        private const val MEDIA_TYPE = "music"
        private const val COUNTRY = "cl"
        private const val SONG = "song"
    }

    // region Observables
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var isError: ObservableBoolean = ObservableBoolean(false)
    var nextPageURL: ObservableField<String> = ObservableField(String.empty())
    var keyword: ObservableField<String> = ObservableField(String.empty())
    var offset: ObservableInt = ObservableInt(0)

    var seekPosition: ObservableInt = ObservableInt(0)

    val isFirstTimePlayed: ObservableBoolean = ObservableBoolean(true)
    var onPauseButton: ObservableBoolean = ObservableBoolean(false)
    // endregion

    private val getSearchMusicLiveData: MutableLiveData<Resource<SearchMusicViewData>> =
        MutableLiveData()

    var trackListLayout: Int = R.layout.view_cell_search_track_item
    var trackListResult: ObservableField<ArrayList<TrackResultItemViewData?>> =
        ObservableField(
            arrayListOf()
        )

    fun getSearchResultsLiveData(): MutableLiveData<Resource<SearchMusicViewData>> =
        getSearchMusicLiveData

    fun getSearchSongsResultsData() {
        nextPageURL.set(
            String.format(
                URL_FORMAT,
                SEARCH_URL + keyword.get(), offset.get(),
                MEDIA_TYPE,
                PAGE_SIZE,
                COUNTRY,
                SONG
            )
        )

        isLoading.set(true)
        getSearchMusicLiveData.postValue(Resource(ResourceState.LOADING, null, null))

        return getSearchMusicUseCase.execute(
            BaseSubscriber(
                context?.applicationContext, this, searchViewDataMapper,
                getSearchMusicLiveData, isLoading, isError
            ), nextPageURL.get()
        )
    }
}