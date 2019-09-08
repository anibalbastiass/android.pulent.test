package com.anibalbastias.android.pulentapp.ui.search

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.appComponent
import com.anibalbastias.android.pulentapp.base.module.ViewModelFactory
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.base.view.ResourceState
import com.anibalbastias.android.pulentapp.databinding.FragmentSearchMusicBinding
import com.anibalbastias.android.pulentapp.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.ui.search.model.SearchResultItemViewData
import com.anibalbastias.android.pulentapp.ui.search.viewmodel.SearchMusicViewModel
import com.anibalbastias.android.pulentapp.util.*
import javax.inject.Inject

class SearchFragment : BaseModuleFragment() {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_search_music

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
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
        binding?.lifecycleOwner = this

        initViewModel()
        initToolbar()

        searchViewModel.searchListDataView?.let {
            setPageData(it)
        } ?: searchViewModel.getSeriesData()
    }

    private fun initViewModel() {
        searchViewModel.getPageLiveData().initObserver(this@SearchFragment) {
            if (it != null) {
                this.handlePageData(it.status, it.data, it.message)
            }
        }
    }

    private fun handlePageData(status: ResourceState, data: SearchMusicViewData?, message: String?) {
        when (status) {
            ResourceState.DEFAULT -> {
            }
            ResourceState.LOADING -> showLoadingView()
            ResourceState.SUCCESS -> setPageData(data!!)
            ResourceState.ERROR -> showErrorView(message)
            else -> {
            }
        }
    }

    private fun showErrorView(message: String?) {
        binding?.searchLoader?.gone()
    }

    private fun showLoadingView() {
        binding?.searchLoader?.visible()
    }

    private fun setPageData(data: SearchMusicViewData) {
        binding?.searchLoader?.gone()

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