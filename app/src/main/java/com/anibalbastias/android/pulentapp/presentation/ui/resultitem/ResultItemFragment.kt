package com.anibalbastias.android.pulentapp.presentation.ui.resultitem

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.transition.TransitionInflater
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.databinding.FragmentResultDetailMainBinding
import com.anibalbastias.android.pulentapp.presentation.appComponent
import com.anibalbastias.android.pulentapp.presentation.getAppContext
import com.anibalbastias.android.pulentapp.presentation.ui.resultitem.interfaces.TrackListener
import com.anibalbastias.android.pulentapp.presentation.ui.resultitem.viewmodel.ResultItemViewModel
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.TrackResultItemViewData
import com.anibalbastias.android.pulentapp.presentation.util.applyFontForToolbarTitle
import com.anibalbastias.android.pulentapp.presentation.util.implementObserver
import com.anibalbastias.android.pulentapp.presentation.util.setArrowUpToolbar


/**
 * Created by anibalbastias on 2019-09-08.
 */

class ResultItemFragment : BaseModuleFragment(),
    TrackListener<TrackResultItemViewData> {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_result_detail_main

    private lateinit var binding: FragmentResultDetailMainBinding

    private lateinit var resultItemViewModel: ResultItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        appComponent().inject(this)
        navBaseViewModel = getViewModel(viewModelFactory)
        resultItemViewModel = getViewModel(viewModelFactory)
        sharedViewModel = activity!!.getViewModel(SavedStateViewModelFactory(getAppContext(), this))
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavController(this@ResultItemFragment.view)

        binding = DataBindingUtil.bind<ViewDataBinding>(view) as FragmentResultDetailMainBinding
        binding.sharedViewModel = sharedViewModel
        binding.resultItemViewModel = resultItemViewModel
        binding.trackListener = this
        binding.lifecycleOwner = this

        initViewModel()
        initToolbar()

        resultItemViewModel.apply {
            getSearchResultsLiveData().value?.data?.let {
                setResultsData(it)
            } ?: run {
                isLoading.set(true)
                sharedViewModel.apply {
                    keyword.set("${resultItemViewData.artistName} ${resultItemViewData.collectionName}}")
                }
                getSearchSongsResultsData()
            }
        }
    }

    private fun setResultsData(result: SearchMusicViewData) {
        resultItemViewModel.isLoading.set(false)
        resultItemViewModel.trackListResult.set(
            result.results as? ArrayList<TrackResultItemViewData?>
        )
    }

    override fun onClickView(view: View, item: TrackResultItemViewData) {

    }

    override fun onPlayPauseTrack(item: TrackResultItemViewData) {

    }

    private fun initViewModel() {
        implementObserver(resultItemViewModel.getSearchResultsLiveData(),
            successBlock = { viewData -> setResultsData(viewData) },
            loadingBlock = { showLoadingView() },
            errorBlock = { showErrorView(it) })
    }

    private fun showErrorView(message: String?) {
        resultItemViewModel.isLoading.set(false)
    }

    private fun showLoadingView() {
        resultItemViewModel.isLoading.set(true)
    }

    private fun initToolbar() {
        binding.toolbar?.run {
            applyFontForToolbarTitle(activity!!)
            setArrowUpToolbar(activity!!)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }
    }
}