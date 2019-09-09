package com.anibalbastias.android.pulentapp.base.module.component

import com.anibalbastias.android.pulentapp.presentation.PulentApplication
import com.anibalbastias.android.pulentapp.base.module.executor.JobExecutor
import com.anibalbastias.android.pulentapp.base.module.executor.UIThread


interface BaseApplicationComponent {

    fun inject(application: PulentApplication)
    fun threadExecutor(): JobExecutor
    fun postExecutionThread(): UIThread
}