package com.anibalbastias.android.pulentapp.presentation.ui.search

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.presentation.appComponent
import com.anibalbastias.android.pulentapp.presentation.util.adapter.base.BaseBindClickHandler
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.databinding.FragmentSearchMusicBinding
import com.anibalbastias.android.pulentapp.presentation.ui.search.adapter.SearchMusicAdapter
import com.anibalbastias.android.pulentapp.presentation.ui.search.interfaces.SearchListener
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.viewmodel.SearchMusicViewModel
import com.anibalbastias.android.pulentapp.presentation.util.*
import com.anibalbastias.android.pulentapp.presentation.util.widget.ClearableEditText
import com.anibalbastias.android.pulentapp.presentation.util.widget.WrapContentLinearLayoutManager
import javax.inject.Inject

class SearchFragment : BaseModuleFragment(),
    SearchListener,
    ClearableEditText.Listener,
    BaseBindClickHandler<SearchResultItemViewData> {

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
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = DataBindingUtil.bind<ViewDataBinding>(view) as FragmentSearchMusicBinding

        binding?.searchViewModel = searchViewModel
        binding?.finderCallback = this
        binding?.clearableListener = this
        binding?.lifecycleOwner = this

        initViewModel()
        initToolbar()

        searchViewModel.apply {
            searchListDataView?.let {
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
    }

    private fun showLoadingView() {
        searchViewModel.isLoading.set(true)
    }

    private fun setResultsData(items: SearchMusicViewData) {

        searchViewModel.isLoading.set(false)

        binding?.searchListSwipeRefreshLayout?.isRefreshing = false

        if (searchViewModel.isLoadingMorePages.compareAndSet(true, false)) {
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
                        setAdapterByData()
                    } else {
                        // Show Empty View
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
        binding?.searchListRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                searchViewModel.itemPosition.set((binding?.searchListRecyclerView?.layoutManager as
                        WrapContentLinearLayoutManager).findFirstVisibleItemPosition())

                if (searchViewModel.nextPageURL.get()?.isNotEmpty()!!) {
                    val visibleItemCount = binding?.searchListRecyclerView?.layoutManager?.childCount
                    val totalItemCount = binding?.searchListRecyclerView?.layoutManager?.itemCount
                    val firstVisibleItemPosition = (binding?.searchListRecyclerView?.layoutManager as WrapContentLinearLayoutManager).findFirstVisibleItemPosition()

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

    }

    override fun onClickView(view: View, item: SearchResultItemViewData) {

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