package com.anibalbastias.android.pulentapp.component

import com.anibalbastias.android.pulentapp.module.ApplicationModule
import com.anibalbastias.android.pulentapp.module.PulentAPIModule
import com.anibalbastias.android.pulentapp.module.ViewModelModule
import com.anibalbastias.android.pulentapp.MainActivity
import com.anibalbastias.android.pulentapp.base.module.component.BaseApplicationComponent
import com.anibalbastias.android.pulentapp.module.PulentRepositoryModule
import com.anibalbastias.android.pulentapp.ui.entry.EntryFragment
import com.anibalbastias.android.pulentapp.ui.search.SearchFragment
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
}