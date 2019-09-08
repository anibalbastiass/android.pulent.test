package com.anibalbastias.android.pulentapp.presentation.ui.search.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.anibalbastias.android.pulentapp.domain.search.usecase.GetSearchMusicUseCase
import com.anibalbastias.android.pulentapp.base.subscriber.BaseSubscriber
import com.anibalbastias.android.pulentapp.base.view.BaseViewModel
import com.anibalbastias.android.pulentapp.base.view.Resource
import com.anibalbastias.android.pulentapp.base.view.ResourceState
import com.anibalbastias.android.pulentapp.presentation.context
import com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.SearchViewDataMapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.util.empty
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class SearchMusicViewModel @Inject constructor(
    private val getSearchMusicUseCase: GetSearchMusicUseCase,
    private val searchViewDataMapper: SearchViewDataMapper
) : BaseViewModel() {

    companion object {
        const val PAGE_SIZE = 20
        private const val URL_FORMAT = "%s&offset=%d&mediaType=%s&limit=%d&country=%s"
        private const val SEARCH_URL = "search?term="

        private const val MEDIA_TYPE = "music"
        private const val COUNTRY = "cl"
    }

    // region Observables
    var isLoading: ObservableBoolean = ObservableBoolean(false)
    var isError: ObservableBoolean = ObservableBoolean(false)

    var keyword: ObservableField<String> = ObservableField(String.empty())
    var offset: ObservableInt = ObservableInt(0)

    var nextPageURL: ObservableField<String> = ObservableField(String.empty())
    var lastPosition: ObservableInt = ObservableInt(0)
    var itemPosition: ObservableInt = ObservableInt(0)
    // endregion

    var isLoadingMorePages: AtomicBoolean = AtomicBoolean(false)

    //region LiveData vars
    private val getSearchMusicLiveData: MutableLiveData<Resource<SearchMusicViewData>> =
        MutableLiveData()
    private val pageDataLiveData: MutableLiveData<SearchMusicViewData> = MutableLiveData()

    private val searchResultListLiveData: MutableLiveData<ArrayList<SearchResultItemViewData?>?> =
        MutableLiveData()
    //endregion

    var searchResultListPaginationViewData: ArrayList<SearchResultItemViewData?>?
        get() = searchResultListLiveData.value
        set(value) {
            searchResultListLiveData.value = value
        }

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

    fun getSearchResultsData(isPaging: Boolean? = false) {
        nextPageURL.set(String.format(URL_FORMAT,
            SEARCH_URL + keyword.get(), offset.get(), MEDIA_TYPE, PAGE_SIZE, COUNTRY))

        if(!isPaging!!) {
            isLoading.set(true)
            getSearchMusicLiveData.postValue(Resource(ResourceState.LOADING, null, null))
        }

        return getSearchMusicUseCase.execute(
            BaseSubscriber(
                context?.applicationContext, this, searchViewDataMapper,
                getSearchMusicLiveData, isLoading, isError
            ), nextPageURL.get()
        )
    }

    fun onKeywordTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        keyword.set(s.toString())
    }

    fun addMoreItemsInProgressData(t: SearchMusicViewData) {
        val isFirstPageLoad = nextPageURL.get()?.isEmpty()
        nextPageURL.set("${t.resultCount}")

        if (!isFirstPageLoad!!) {
            //Pagination Loaded
            val oldList = searchResultListPaginationViewData

            t.results?.let {
                if (!it.filterNotNull().isNullOrEmpty()) {
                    val addSuccessful = oldList?.addAll(it.toList())
                    if (addSuccessful == true) {
                        searchResultListPaginationViewData = oldList
                    }
                }
            }
        } else {
            //FirstPage Loaded
            searchResultListPaginationViewData = t.results
        }
    }
}