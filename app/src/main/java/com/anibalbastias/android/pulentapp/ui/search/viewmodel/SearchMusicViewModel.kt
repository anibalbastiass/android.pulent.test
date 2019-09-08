package com.anibalbastias.android.pulentapp.ui.search.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.anibalbastias.android.pulentapp.base.api.domain.search.usecase.GetSearchMusicUseCase
import com.anibalbastias.android.pulentapp.base.subscriber.BaseSubscriber
import com.anibalbastias.android.pulentapp.base.view.BaseViewModel
import com.anibalbastias.android.pulentapp.base.view.Resource
import com.anibalbastias.android.pulentapp.base.view.ResourceState
import com.anibalbastias.android.pulentapp.context
import com.anibalbastias.android.pulentapp.ui.search.mapper.SearchViewDataMapper
import com.anibalbastias.android.pulentapp.ui.search.model.SearchMusicViewData
import javax.inject.Inject

class SearchMusicViewModel @Inject constructor(
    private val getSearchMusicUseCase: GetSearchMusicUseCase,
    private val searchViewDataMapper: SearchViewDataMapper
) : BaseViewModel() {

    // region Observables
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var isError: ObservableBoolean = ObservableBoolean(false)
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

    fun getPageLiveData(): MutableLiveData<Resource<SearchMusicViewData>> = getSearchMusicLiveData

    fun getSeriesData() {
        val params = mutableMapOf<String, String>()
        params["term"] = "nightwish"
        params["offset"] = "60"
        params["mediaType"] = "music"
        params["limit"] = "20"
        params["country"] = "cl"

        getSearchMusicLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        return getSearchMusicUseCase.execute(
            BaseSubscriber(context?.applicationContext, this, searchViewDataMapper,
                getSearchMusicLiveData, isLoading, isError), params)
    }
}