package com.anibalbastias.android.pulentapp.presentation.ui.search.viewmodel

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.MutableLiveData
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.domain.search.usecase.GetSearchMusicUseCase
import com.anibalbastias.android.pulentapp.base.subscriber.BaseSubscriber
import com.anibalbastias.android.pulentapp.base.view.BaseViewModel
import com.anibalbastias.android.pulentapp.base.view.Resource
import com.anibalbastias.android.pulentapp.base.view.ResourceState
import com.anibalbastias.android.pulentapp.domain.search.db.RealmManager
import com.anibalbastias.android.pulentapp.domain.search.model.ResultItemRealmData
import com.anibalbastias.android.pulentapp.domain.search.model.SearchRecentRealmData
import com.anibalbastias.android.pulentapp.presentation.context
import com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.realm.SearchResultItemRealmMapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.SearchViewDataMapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.util.empty
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import com.anibalbastias.android.pulentapp.presentation.ui.search.interfaces.GetSearchRecentsListener
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.CollectionResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.TrackResultItemViewData
import io.realm.RealmList
import kotlin.collections.ArrayList


class SearchMusicViewModel @Inject constructor(
    private val getSearchMusicUseCase: GetSearchMusicUseCase,
    private val searchViewDataMapper: SearchViewDataMapper,
    private val searchResultItemRealmMapper: SearchResultItemRealmMapper
) : BaseViewModel() {

    companion object {
        const val PAGE_SIZE = 20
        private const val URL_FORMAT = "%s&offset=%d&mediaType=%s&limit=%d&country=%s&entity=%s&sort=recent"
//        private const val URL_FORMAT = "%s&offset=%d&&limit=%d&country=%s&entity=%s&sort=recent"
        private const val SEARCH_URL = "search?term="

        private const val MEDIA_TYPE = "music"
        private const val COUNTRY = "cl"
        private const val ALBUM = "album"
        private const val SONG = "song"
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

    var searchRecentList: ObservableField<ArrayList<SearchRecentRealmData>> = ObservableField(
        arrayListOf()
    )
    var searchRecentLayout: Int = R.layout.view_cell_search_recent_search_item
    // endregion

    var isLoadingMorePages: AtomicBoolean = AtomicBoolean(false)

    //region LiveData vars
    private val getSearchMusicLiveData: MutableLiveData<Resource<SearchMusicViewData>> =
        MutableLiveData()

    private val collectionsResultListLiveData: MutableLiveData<ArrayList<CollectionResultItemViewData?>?> =
        MutableLiveData()

    private val tracksResultListLiveData: MutableLiveData<ArrayList<TrackResultItemViewData?>?> =
        MutableLiveData()
    //endregion

    var searchCollectionResultListPaginationViewData: ArrayList<CollectionResultItemViewData?>?
        get() = collectionsResultListLiveData.value
        set(value) {
            collectionsResultListLiveData.value = value
        }

    var searchTrackResultListPaginationViewData: ArrayList<TrackResultItemViewData?>?
        get() = tracksResultListLiveData.value
        set(value) {
            tracksResultListLiveData.value = value
        }

    override fun onCleared() {
        getSearchMusicUseCase.dispose()
        super.onCleared()
    }

    fun getSearchResultsLiveData(): MutableLiveData<Resource<SearchMusicViewData>> =
        getSearchMusicLiveData

    fun getSearchAlbumsResultsData(isPaging: Boolean? = false) {
        nextPageURL.set(
            String.format(
                URL_FORMAT,
                SEARCH_URL + keyword.get(), offset.get(), MEDIA_TYPE, PAGE_SIZE, COUNTRY, ALBUM
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

    fun getSearchSongsResultsData(isPaging: Boolean? = false) {
        nextPageURL.set(
            String.format(
                URL_FORMAT,
                SEARCH_URL + keyword.get(), offset.get(), MEDIA_TYPE, PAGE_SIZE, COUNTRY, SONG
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
            val oldList = searchCollectionResultListPaginationViewData

            t.results?.let {
                if (!it.filterNotNull().isNullOrEmpty()) {
                    val addSuccessful = oldList?.addAll(it.toList())
                    if (addSuccessful == true) {
                        searchCollectionResultListPaginationViewData = oldList
                    }
                }
            }
        } else {
            //FirstPage Loaded
            searchCollectionResultListPaginationViewData = t.results
        }
    }

    fun putRecentSearchItem(
        keyword: String,
        itemsVD: ArrayList<CollectionResultItemViewData?>
    ) {

        // Save Search
        val searchItem = SearchRecentRealmData()
        searchItem.keyword = keyword

        val realmList = RealmList<ResultItemRealmData?>()
        itemsVD.map { item ->
            searchResultItemRealmMapper.keywordValue = keyword
            realmList.add(searchResultItemRealmMapper.executeMapping(item))
        }

        searchItem.results = realmList
        RealmManager.createSearchRecentDao().save(searchItem)
    }

    fun loadRecentSearchListAsync(callback: GetSearchRecentsListener?) {
        val dataList = RealmManager.createSearchRecentDao().loadAllAsync()
        dataList.addChangeListener { t, changeSet ->
            callback?.onGetRecentSearchFromRealm(t)
        }
    }

    fun clearRecentSearchList(callback: GetSearchRecentsListener?) {
        RealmManager.createSearchRecentDao().removeAll()
        callback?.onGetRecentSearchFromRealm(null)
    }

    fun getSearchResultFromRealm(keyword: String?): SearchMusicViewData {
        val result = RealmManager.createSearchRecentDao().loadByKeyword(keyword!!)

        val itemsResult = arrayListOf<CollectionResultItemViewData?>()
        result.results?.forEach {
            searchResultItemRealmMapper.inverseExecuteMapping(it)
        }
        return SearchMusicViewData(
            resultCount = itemsResult.size,
            results = itemsResult)
    }
}