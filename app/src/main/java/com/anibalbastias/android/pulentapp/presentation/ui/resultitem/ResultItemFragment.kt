package com.anibalbastias.android.pulentapp.presentation.ui.resultitem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.presentation.appComponent
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.databinding.FragmentResultBinding
import com.anibalbastias.android.pulentapp.presentation.getAppContext
import com.anibalbastias.android.pulentapp.presentation.util.setImageUrl

/**
 * Created by anibalbastias on 2019-09-08.
 */

class ResultItemFragment : BaseModuleFragment() {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_result

    private lateinit var binding: FragmentResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

        appComponent().inject(this)
        navBaseViewModel = getViewModel(viewModelFactory)
        sharedViewModel = activity!!.getViewModel(SavedStateViewModelFactory(getAppContext(), this))
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNavController(this@ResultItemFragment.view)

        binding = DataBindingUtil.bind<ViewDataBinding>(view) as FragmentResultBinding
        binding.lifecycleOwner = this

        binding.resultItemImage.setImageUrl(sharedViewModel.resultItemViewData.artworkUrl100)
    }
}