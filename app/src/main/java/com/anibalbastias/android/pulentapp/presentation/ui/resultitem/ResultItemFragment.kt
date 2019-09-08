package com.anibalbastias.android.pulentapp.presentation.ui.resultitem

import android.os.Bundle
import com.anibalbastias.android.pulentapp.R
import com.anibalbastias.android.pulentapp.presentation.appComponent
import com.anibalbastias.android.pulentapp.base.view.BaseModuleFragment

/**
 * Created by anibalbastias on 2019-09-08.
 */

class ResultItemFragment : BaseModuleFragment() {

    override fun tagName(): String = this::class.java.simpleName
    override fun layoutId(): Int = R.layout.fragment_result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent().inject(this)
//        searchViewModel = getViewModel(viewModelFactory)
        setHasOptionsMenu(true)
    }

}