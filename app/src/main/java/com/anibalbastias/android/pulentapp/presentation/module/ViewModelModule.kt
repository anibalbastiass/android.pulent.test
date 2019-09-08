package com.anibalbastias.android.pulentapp.presentation.module

import androidx.lifecycle.ViewModel
import com.anibalbastias.android.pulentapp.base.module.ViewModelKey
import com.anibalbastias.android.pulentapp.base.module.module.BaseViewModelModule
import com.anibalbastias.android.pulentapp.base.view.NavBaseViewModel
import com.anibalbastias.android.pulentapp.presentation.ui.search.viewmodel.SearchMusicViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule : BaseViewModelModule() {

    @Binds
    @IntoMap
    @ViewModelKey(SearchMusicViewModel::class)
    internal abstract fun seriesViewModel(viewModel: SearchMusicViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NavBaseViewModel::class)
    internal abstract fun navBaseViewModel(viewModel: NavBaseViewModel): ViewModel

}