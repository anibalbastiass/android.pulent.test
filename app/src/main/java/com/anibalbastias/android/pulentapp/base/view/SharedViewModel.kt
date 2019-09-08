package com.anibalbastias.android.pulentapp.base.view

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.CollectionResultItemViewData
import javax.inject.Inject

/**
 * Created by anibalbastias on 2019-09-08.
 */

class SharedViewModel @Inject constructor(val state: SavedStateHandle) : BaseViewModel() {

    // Keep the key as a constant
    companion object {
        private const val RESULT_ITEM_KEY = "resultItemKey"
    }

    private val savedStateHandle = state

    var isLoading: ObservableBoolean = ObservableBoolean(false)

    private val resultItemLiveData: MutableLiveData<CollectionResultItemViewData> = savedStateHandle.getLiveData(RESULT_ITEM_KEY)

    var resultItemViewData: CollectionResultItemViewData
        get() = resultItemLiveData.value ?: CollectionResultItemViewData()
        set(value) {
            resultItemLiveData.value = value
        }
}