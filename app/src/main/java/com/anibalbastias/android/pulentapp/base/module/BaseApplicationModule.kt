package com.anibalbastias.android.pulentapp.base.module

import com.anibalbastias.android.pulentapp.PulentApplication
import com.anibalbastias.android.pulentapp.base.api.domain.base.executor.APIPostExecutionThread
import com.anibalbastias.android.pulentapp.base.api.domain.base.executor.APIThreadExecutor
import com.anibalbastias.android.pulentapp.base.module.executor.JobExecutor
import com.anibalbastias.android.pulentapp.base.module.executor.UIThread
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class BaseApplicationModule(private val application: PulentApplication) {

    @Provides
    @Singleton
    fun provideApp(): PulentApplication = application

    @Provides
    @Singleton
    fun provideAPIThreadExecutor(jobExecutor: JobExecutor): APIThreadExecutor = jobExecutor

    @Provides
    @Singleton
    fun provideAPIPostExecutionThread(uiThread: UIThread): APIPostExecutionThread = uiThread
}