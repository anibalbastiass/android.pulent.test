package com.anibalbastias.android.pulentapp.ui.search.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.anibalbastias.android.pulentapp.base.api.domain.search.usecase.GetSearchMusicUseCase
import com.anibalbastias.android.pulentapp.base.subscriber.BaseSubscriber
import com.anibalbastias.android.pulentapp.base.view.BaseViewModel
import com.anibalbastias.android.pulentapp.base.view.Resource
import com.anibalbastias.android.pulentapp.base.view.ResourceState
import com.anibalbastias.android.pulentapp.context
import com.anibalbastias.android.pulentapp.ui.search.mapper.SearchViewDataMapper
import com.anibalbastias.android.pulentapp.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.util.empty
import javax.inject.Inject

class SearchMusicViewModel @Inject constructor(
    private val getSearchMusicUseCase: GetSearchMusicUseCase,
    private val searchViewDataMapper: SearchViewDataMapper
) : BaseViewModel() {

    companion object {
        private const val PAGE_SIZE = 20
        private const val MEDIA_TYPE = "music"
        private const val COUNTRY = "cl"

        // Hash Map keys
        private const val TERM_KEY = "term"
        private const val OFFSET_KEY = "offset"
        private const val MEDIA_TYPE_KEY = "mediaType"
        private const val LIMIT_KEY = "limit"
        private const val COUNTRY_KEY = "country"
    }

    // region Observables
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var isError: ObservableBoolean = ObservableBoolean(false)

    var keyword: ObservableField<String> = ObservableField(String.empty())
    var offset: ObservableInt = ObservableInt(0)
    // endregion

    //region LiveData vars
    private val getSearchMusicLiveData: MutableLiveData<Resource<SearchMusicViewData>> =
        MutableLiveData()
    private val pageDataLiveData: MutableLiveData<SearchMusicViewData> = MutableLiveData()
    //endregion

    var searchListDataView: SearchMusicViewData?
        get() = pageDataLiveData.value
        set(value) {
            pageDataLiveData.value = value
        }

    override fun onCleared() {
        super.onCleared()
        getSearchMusicUseCase.dispose()
    }

    fun setPageDefaultState() {
        getSearchMusicLiveData.value = Resource(ResourceState.DEFAULT, null, null)
    }

    fun getSearchResultsLiveData(): MutableLiveData<Resource<SearchMusicViewData>> =
        getSearchMusicLiveData

    fun getSeriesData() {
        val params = mutableMapOf<String, String>()
        params[TERM_KEY] = keyword.get() ?: String.empty()
        params[OFFSET_KEY] = "${offset.get()}"
        params[MEDIA_TYPE_KEY] = MEDIA_TYPE
        params[LIMIT_KEY] = "$PAGE_SIZE"
        params[COUNTRY_KEY] = COUNTRY

        isLoading.set(true)
        getSearchMusicLiveData.postValue(Resource(ResourceState.LOADING, null, null))

        return getSearchMusicUseCase.execute(
            BaseSubscriber(
                context?.applicationContext, this, searchViewDataMapper,
                getSearchMusicLiveData, isLoading, isError
            ), params
        )
    }
}