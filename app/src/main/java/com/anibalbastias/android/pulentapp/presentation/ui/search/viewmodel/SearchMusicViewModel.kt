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
import com.anibalbastias.android.pulentapp.domain.search.db.RealmManager
import com.anibalbastias.android.pulentapp.domain.search.model.SearchRecentRealmData
import com.anibalbastias.android.pulentapp.presentation.context
import com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.SearchResultItemRealmMapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.SearchViewDataMapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.util.empty
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import com.anibalbastias.android.pulentapp.presentation.ui.search.interfaces.GetSearchRecentsListener
import java.util.*
import kotlin.collections.ArrayList


class SearchMusicViewModel @Inject constructor(
    private val getSearchMusicUseCase: GetSearchMusicUseCase,
    private val searchViewDataMapper: SearchViewDataMapper,
    private val searchResultItemRealmMapper: SearchResultItemRealmMapper
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
    var isEmpty: ObservableBoolean = ObservableBoolean(false)

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

    private val searchResultListLiveData: MutableLiveData<ArrayList<SearchResultItemViewData?>?> =
        MutableLiveData()
    //endregion

    var searchResultListPaginationViewData: ArrayList<SearchResultItemViewData?>?
        get() = searchResultListLiveData.value
        set(value) {
            searchResultListLiveData.value = value
        }

    override fun onCleared() {
        getSearchMusicUseCase.dispose()
        super.onCleared()
    }

    fun getSearchResultsLiveData(): MutableLiveData<Resource<SearchMusicViewData>> =
        getSearchMusicLiveData

    fun getSearchResultsData(isPaging: Boolean? = false) {
        nextPageURL.set(
            String.format(
                URL_FORMAT,
                SEARCH_URL + keyword.get(), offset.get(), MEDIA_TYPE, PAGE_SIZE, COUNTRY
            )
        )

        if (!isPaging!!) {
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

    fun putRecentSearchItem(
        keyword: String,
        itemsVD: java.util.ArrayList<SearchResultItemViewData?>
    ) {
        val searchItem = SearchRecentRealmData()
        searchItem.id = Date().time.toInt()
        searchItem.keyword = keyword

        val itemsResult = itemsVD.map {
            searchResultItemRealmMapper.executeMapping(it)!!
        }
//        searchItem.results = itemsResult

        RealmManager.createSearchResultItemDao().save(itemsResult)
        RealmManager.createSearchRecentDao().save(searchItem)
    }

    fun loadRecentSearchListAsync(callback: GetSearchRecentsListener?) {
        val dataList = RealmManager.createSearchRecentDao().loadAllAsync()
        dataList.addChangeListener { t, changeSet ->
            callback?.onGetRecentSearchFromRealm(t)
        }
    }
}