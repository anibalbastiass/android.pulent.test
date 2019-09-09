package com.anibalbastias.android.pulentapp.presentation.component

import com.anibalbastias.android.pulentapp.presentation.module.ApplicationModule
import com.anibalbastias.android.pulentapp.presentation.module.PulentAPIModule
import com.anibalbastias.android.pulentapp.presentation.module.ViewModelModule
import com.anibalbastias.android.pulentapp.presentation.MainActivity
import com.anibalbastias.android.pulentapp.base.module.component.BaseApplicationComponent
import com.anibalbastias.android.pulentapp.presentation.module.PulentRepositoryModule
import com.anibalbastias.android.pulentapp.presentation.ui.entry.EntryFragment
import com.anibalbastias.android.pulentapp.presentation.ui.resultitem.ResultItemFragment
import com.anibalbastias.android.pulentapp.presentation.ui.search.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        PulentRepositoryModule::class,
        ViewModelModule::class,
        PulentAPIModule::class]
)

interface ApplicationComponent : BaseApplicationComponent, FragmentInjector {
    fun inject(mainActivity: MainActivity)
}

interface FragmentInjector {
    fun inject(entryFragment: EntryFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(resultItemFragment: ResultItemFragment)
}