package com.anibalbastias.android.pulentapp.presentation.ui.resultitem

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.transition.TransitionInflater
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.base.module.getViewModel
import com.anibalbastias.android.pulentapp.presentation.appComponent
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment
import com.anibalbastias.android.pulentapp.databinding.FragmentResultDetailMainBinding
import com.anibalbastias.android.pulentapp.presentation.getAppContext
import android.provider.MediaStore.Audio
import com.anibalbastias.android.pulentapp.presentation.service.MediaPlayerService


/**
 * Created by anibalbastias on 2019-09-08.
 */

class ResultItemFragment : BaseModuleFragment() {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_result_detail_main

    private lateinit var binding: FragmentResultDetailMainBinding

    companion object {
        val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
    }

    private val player: MediaPlayerService? = null
    var serviceBound = false
    var audioList: ArrayList<Audio>? = null


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

        binding = DataBindingUtil.bind<ViewDataBinding>(view) as FragmentResultDetailMainBinding
        binding.sharedViewModel = sharedViewModel
        binding.lifecycleOwner = this
    }
}