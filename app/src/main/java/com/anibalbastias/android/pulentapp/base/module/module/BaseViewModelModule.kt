package com.anibalbastias.android.pulentapp.base.module.module

import androidx.lifecycle.ViewModelProvider
import com.anibalbastias.android.pulentapp.base.module.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class BaseViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}