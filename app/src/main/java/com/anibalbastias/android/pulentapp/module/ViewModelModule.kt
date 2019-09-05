package com.anibalbastias.android.pulentapp.module

import androidx.lifecycle.ViewModel
import com.anibalbastias.android.pulentapp.base.module.ViewModelKey
import com.anibalbastias.android.pulentapp.base.module.module.BaseViewModelModule
import com.anibalbastias.android.pulentapp.ui.series.viewmodel.SearchMusicViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule : BaseViewModelModule() {

    @Binds
    @IntoMap
    @ViewModelKey(SearchMusicViewModel::class)
    internal abstract fun seriesViewModel(viewModel: SearchMusicViewModel): ViewModel

}