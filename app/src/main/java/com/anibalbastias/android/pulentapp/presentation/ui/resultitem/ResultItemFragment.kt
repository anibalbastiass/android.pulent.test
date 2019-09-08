package com.anibalbastias.android.pulentapp.presentation.ui.resultitem

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.SavedStateViewModelFactory
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.presentation.appComponent
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.databinding.FragmentResultBinding
import com.anibalbastias.android.pulentapp.presentation.getAppContext

/**
 * Created by anibalbastias on 2019-09-08.
 */

class ResultItemFragment : BaseModuleFragment() {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_result

    private lateinit var binding: FragmentResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}