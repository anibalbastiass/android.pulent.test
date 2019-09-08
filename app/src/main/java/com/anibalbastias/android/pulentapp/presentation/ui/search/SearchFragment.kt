package com.anibalbastias.android.pulentapp.presentation.ui.search

import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.android.pulentapp.presentation.appComponent
import com.anibalbastias.android.pulentapp.presentation.util.adapter.base.BaseBindClickHandler
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.databinding.FragmentSearchMusicBinding
import com.anibalbastias.android.pulentapp.presentation.getAppContext
import com.anibalbastias.android.pulentapp.presentation.ui.search.adapter.SearchMusicAdapter
import com.anibalbastias.android.pulentapp.presentation.ui.search.interfaces.SearchListener
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.viewmodel.SearchMusicViewModel
import com.anibalbastias.android.pulentapp.presentation.util.*
import com.anibalbastias.android.pulentapp.presentation.util.widget.ClearableEditText
import com.anibalbastias.android.pulentapp.presentation.util.widget.WrapContentLinearLayoutManager
import javax.inject.Inject
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.domain.search.model.SearchRecentRealmData
import com.anibalbastias.android.pulentapp.presentation.ui.search.interfaces.GetSearchRecentsListener
import com.anibalbastias.android.pulentapp.presentation.ui.search.interfaces.SearchRecentsListener
import io.realm.RealmResults


class SearchFragment : BaseModuleFragment(),
    SearchListener,
    ClearableEditText.Listener,
    BaseBindClickHandler<SearchResultItemViewData>,
    GetSearchRecentsListener,
    SearchRecentsListener<SearchResultItemViewData> {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_search_music

    private lateinit var searchViewModel: SearchMusicViewModel

    @Inject
    lateinit var searchMusicAdapter: SearchMusicAdapter

    private var binding: FragmentSearchMusicBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent().inject(this)
        searchViewModel = getViewModel(viewModelFactory)
        navBaseViewModel = getViewModel(viewModelFactory)
        sharedViewModel = activity!!.getViewModel(SavedStateViewModelFactory(getAppContext(), this))
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavController(this@SearchFragment.view)

        binding = DataBindingUtil.bind<ViewDataBinding>(view) as FragmentSearchMusicBinding

        binding?.searchViewModel = searchViewModel
        binding?.finderCallback = this
        binding?.recentSearchCallback = this
        binding?.clearableListener = this
        binding?.lifecycleOwner = this

        initViewModel()
        initToolbar()

        searchViewModel.apply {
            getSearchResultsLiveData().value?.data?.let {
                setResultsData(it)
            } ?: run {
                isLoading.set(true)
                getSearchResultsData()
            }

            // Set Swipe Refresh Layout
            binding?.searchListSwipeRefreshLayout?.initSwipe {
                // Reset offset
                offset.set(0)
                getSearchResultsData()
            }
        }
    }

    private fun initViewModel() {
        implementObserver(searchViewModel.getSearchResultsLiveData(),
            successBlock = { viewData -> setResultsData(viewData) },
            loadingBlock = { showLoadingView() },
            errorBlock = { showErrorView(it) })
    }

    override fun onSendActionByHeaderFinder() {
        // Reset offset
        searchViewModel.apply {
            offset.set(0)
            isLoading.set(true)
            getSearchResultsData()
        }
    }

    override fun didClearText() {
        searchViewModel.apply {
            keyword.set(String.empty())
            offset.set(0)
            isLoading.set(true)
            getSearchResultsData()
        }
    }

    private fun showErrorView(message: String?) {
        searchViewModel.isLoading.set(false)
        searchViewModel.isEmpty.set(true)
    }

    private fun showLoadingView() {
        searchViewModel.isEmpty.set(false)
        searchViewModel.isLoading.set(true)
    }

    private fun setResultsData(items: SearchMusicViewData) {
        searchViewModel.isLoading.set(false)
        binding?.searchListSwipeRefreshLayout?.isRefreshing = false

        if (searchViewModel.isLoadingMorePages.compareAndSet(true, false)) {
            searchViewModel.isEmpty.set(false)

            searchMusicAdapter.removeLoadingFooter()
            searchViewModel.addMoreItemsInProgressData(items)

            // Update Next Identifier for pagination
            if (items.results?.isNotEmpty()!!) {
                searchViewModel.nextPageURL.set("${items.resultCount}")
                searchViewModel.lastPosition.set(items.results.size)
            } else {
                searchViewModel.nextPageURL.set(String.empty())
            }

        } else {
            if (items.results?.isNotEmpty()!!) {
                // Set data
                searchViewModel.searchResultListPaginationViewData = items.results

                items.results.let { itemsVD ->
                    binding?.searchListRecyclerView?.visible()

                    if (itemsVD.isNotEmpty()) {
                        searchMusicAdapter.clickHandler = this
                        searchMusicAdapter.items = itemsVD
                        searchViewModel.isEmpty.set(false)

                        searchViewModel.putRecentSearchItem(
                            searchViewModel.keyword.get()!!,
                            itemsVD
                        )

                        setAdapterByData()
                    } else {
                        // Show Empty View
                        showEmptyView()
                    }
                }
            } else {
                searchViewModel.searchResultListPaginationViewData?.apply {
                    clear()

                    searchMusicAdapter.clickHandler = this@SearchFragment
                    searchMusicAdapter.items = this
                }
                setAdapterByData()

                showEmptyView()
            }
        }
    }

    private fun setAdapterByData() {
        context?.let {
            val tdLayoutManager =
                WrapContentLinearLayoutManager(
                    it,
                    LinearLayoutManager.VERTICAL,
                    false
                )

            binding?.searchListRecyclerView?.let { rv ->
                rv.setHasFixedSize(true)
                rv.layoutManager = tdLayoutManager
                rv.adapter = searchMusicAdapter

                paginationForRecyclerScroll()
                rv.scrollToPosition(searchViewModel.itemPosition.get())
                rv.runLayoutAnimation()
            }
        }
    }

    private fun paginationForRecyclerScroll() {
        binding?.searchListRecyclerView?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                searchViewModel.itemPosition.set(
                    (binding?.searchListRecyclerView?.layoutManager as
                            WrapContentLinearLayoutManager).findFirstVisibleItemPosition()
                )

                if (searchViewModel.nextPageURL.get()?.isNotEmpty()!!) {
                    val visibleItemCount =
                        binding?.searchListRecyclerView?.layoutManager?.childCount
                    val totalItemCount = binding?.searchListRecyclerView?.layoutManager?.itemCount
                    val firstVisibleItemPosition =
                        (binding?.searchListRecyclerView?.layoutManager as WrapContentLinearLayoutManager).findFirstVisibleItemPosition()

                    if (!searchViewModel.isLoadingMorePages.get()) {
                        if (visibleItemCount!! + firstVisibleItemPosition >= totalItemCount!! && firstVisibleItemPosition >= 0) {
                            searchViewModel.apply {
                                lastPosition.set(searchResultListPaginationViewData?.size!!)

                                if (searchViewModel.lastPosition.get() >= SearchMusicViewModel.PAGE_SIZE) {
                                    isLoadingMorePages.set(true)

                                    // Increment offset
                                    offset.set(offset.get() + SearchMusicViewModel.PAGE_SIZE)
                                    searchMusicAdapter.addLoadingFooter()

                                    // Fetch next list
                                    isLoading.set(false)
                                    getSearchResultsData(isPaging = true)
                                }
                            }
                        }
                    }
                }
            }
        })
    }

    private fun showEmptyView() {
        searchViewModel.isEmpty.set(true)
        searchViewModel.loadRecentSearchListAsync(this)
    }

    override fun onGetRecentSearchFromRealm(list: RealmResults<SearchRecentRealmData>?) {
        val searchList = arrayListOf<SearchRecentRealmData>()
        list?.forEach { item ->
            searchList.add(item)
        }
        searchViewModel.searchRecentList.set(searchList)
    }

    override fun onClickSearchRecentItem(list: SearchRecentRealmData) {
        searchViewModel.apply {
            keyword.set(list.keyword)
            isLoading.set(true)
            getSearchResultsData()

            binding?.searchFinderContainer?.testDriveFinderEditText?.run {
                val newValue = Editable.Factory.getInstance().newEditable(keyword.get())
                text = newValue
                setSelection(keyword.get()?.length!!)
            }
        }
    }

    override fun onClearSearchItems() {
        searchViewModel.clearRecentSearchList(this)
    }

    private fun getImageViewFromChild(view: View): ImageView {
        val cardView = (view as? CardView)
        val ll1 = (cardView?.getChildAt(0) as? LinearLayout)
        val ll2 = (ll1?.getChildAt(0) as? LinearLayout)
        return (ll2?.getChildAt(0) as? ImageView)!!
    }

    override fun onClickView(view: View, item: SearchResultItemViewData) {
        val extras = FragmentNavigatorExtras(
            getImageViewFromChild(view) to "secondTransitionName"
        )
        ViewCompat.setTransitionName(getImageViewFromChild(view), "secondTransitionName")

        // Share Data
        sharedViewModel.resultItemViewData = item
        nextNavigate(
            nav = SearchFragmentDirections.actionSearchFragmentToResultItemFragment().actionId,
            extras = extras
        )
    }

    private fun initToolbar() {
        binding?.searchToolbar?.run {
            title = "Search Music"
            applyFontForToolbarTitle(activity!!)
            setNoArrowUpToolbar(activity!!)
            setNavigationOnClickListener {
                activity?.finish()
            }
        }
    }
}