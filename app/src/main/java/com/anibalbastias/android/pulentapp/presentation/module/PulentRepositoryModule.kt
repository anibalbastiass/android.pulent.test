package com.anibalbastias.android.pulentapp.presentation.module

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.repository.SearchMusicRepositoryImpl
import com.anibalbastias.android.pulentapp.domain.search.repository.ISeriesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class PulentRepositoryModule {

    @Binds
    abstract fun bindSeriesDataRepository(repository: SearchMusicRepositoryImpl): ISeriesRepository

}