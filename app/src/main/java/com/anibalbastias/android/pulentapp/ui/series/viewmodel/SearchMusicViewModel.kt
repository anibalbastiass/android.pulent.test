package com.anibalbastias.android.pulentapp.ui.series.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.common.search.SearchMusicData
import com.anibalbastias.android.pulentapp.base.api.domain.series.usecase.GetSearchMusicUseCase
import com.anibalbastias.android.pulentapp.base.view.Resource
import com.anibalbastias.android.pulentapp.base.view.ResourceState
import io.reactivex.subscribers.DisposableSubscriber
import javax.inject.Inject

class SearchMusicViewModel @Inject constructor(private val getSearchMusicUseCase: GetSearchMusicUseCase) : ViewModel() {

    //region LiveData vars
    private val getSearchMusicLiveData: MutableLiveData<Resource<SearchMusicData>> =
        MutableLiveData()
    private val pageDataLiveData: MutableLiveData<SearchMusicData> = MutableLiveData()
    //endregion

    var searchListDataView: SearchMusicData?
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

    fun getPageLiveData(): MutableLiveData<Resource<SearchMusicData>> = getSearchMusicLiveData

    fun getSeriesData() {
        val map = mutableMapOf<String, String>()
        map["term"] = "nightwish"
        map["offset"] = "60"
        map["mediaType"] = "music"
        map["limit"] = "20"

        getSearchMusicLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        getSearchMusicUseCase.execute(
            GetSearchMusicSubscriber(),
            map
        )
    }

    inner class GetSearchMusicSubscriber : DisposableSubscriber<SearchMusicData>() {
        override fun onComplete() {}

        override fun onNext(t: SearchMusicData?) {
            t.let {
                getSearchMusicLiveData.postValue(Resource(ResourceState.SUCCESS, t, null))
            }
        }

        override fun onError(t: Throwable?) {
            t?.printStackTrace()
            getSearchMusicLiveData.postValue(Resource(ResourceState.ERROR, null, null))
        }
    }
}