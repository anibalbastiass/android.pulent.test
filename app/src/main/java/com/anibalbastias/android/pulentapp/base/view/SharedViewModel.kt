package com.anibalbastias.android.pulentapp.base.view

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.SavedStateHandle
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-08.
 */

class SharedViewModel @Inject constructor(val state: SavedStateHandle) : BaseViewModel() {

    private val savedStateHandle = state

    var isLoading: ObservableBoolean = ObservableBoolean(false)
}