package com.anibalbastias.android.pulentapp.base.subscriber

import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.anibalbastias.android.pulentapp.base.api.domain.base.Mapper
import com.anibalbastias.android.pulentapp.base.view.BaseViewModel
import com.anibalbastias.android.pulentapp.base.view.Resource
import com.anibalbastias.android.pulentapp.base.view.ResourceState

/**
 * Created by Dan on 26, June, 2019
 * Copyright (c) 2019 Carsales. All rights reserved.
 *
 * Generic base subscriber class.
 * @param context       Context to use optionally in resources or related functionality
 * @param baseViewModel Base view model to fire tracking
 * @param mapper        Mapper to map corresponding data to view data
 * @param liveData      Mutable live data to work with resource states
 * @param isLoading     Optional ObservableBoolean to work most often with databinding in layout
 * @param isError       To do something with databinding when an error has ocurred or not
 */
class BaseSubscriber<VD, D> (context: Context?,
                             val baseViewModel: BaseViewModel,
                             val mapper: Mapper<VD?, D?>,
                             val liveData: MutableLiveData<Resource<VD>>,
                             val isLoading: ObservableBoolean? = null,
                             val isError: ObservableBoolean? = null) : APIExceptionHandlerSubscriber<D>(context) {

    override fun onComplete() {

        isLoading?.set(false)
    }

    override fun onNext(t: D) {

        t?.let {
//            val viewData = mapper.executeMapping(it)
            liveData.postValue(Resource(ResourceState.SUCCESS, mapper.executeMapping(it), null))
//            if (viewData is PageViewData) {
//                baseViewModel.baseTracker.pageTrackingViewData = viewData.tracking
//                baseViewModel.baseTracker.track(viewData.tracking)
//            }
        }
    }

    override fun onAPIError(apiErrorViewData: String?) {
        isLoading?.set(false)
        isError?.set(true)

        liveData.postValue(Resource(ResourceState.ERROR, null, apiErrorViewData))
    }
}