package com.anibalbastias.android.pulentapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.appComponent
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.databinding.FragmentSearchMusicBinding
import com.anibalbastias.android.pulentapp.ui.search.interfaces.SearchListener
import com.anibalbastias.android.pulentapp.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.ui.search.model.SearchResultItemViewData
import com.anibalbastias.android.pulentapp.ui.search.viewmodel.SearchMusicViewModel
import com.anibalbastias.android.pulentapp.util.*

class SearchFragment : BaseModuleFragment(),
    SearchListener,
    ClearableEditText.Listener {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_search_music

    private lateinit var searchViewModel: SearchMusicViewModel

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

    private fun setResultsData(data: SearchMusicViewData) {
        searchViewModel.isLoading.set(false)

        // Set Adapter
        setAdapter(data.results)
    }

    private fun setAdapter(results: List<SearchResultItemViewData?>?) {
        binding?.searchListRecyclerView?.layoutManager = LinearLayoutManager(
            activity!!,
            LinearLayoutManager.VERTICAL, false
        )

//        val adapter = SeriesListAdapter()
//        adapter?.itemCallback = this
//        adapter?.items = results as MutableList<SeriesItemData>?
//        binding?.searchListRecyclerView?.adapter = adapter

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