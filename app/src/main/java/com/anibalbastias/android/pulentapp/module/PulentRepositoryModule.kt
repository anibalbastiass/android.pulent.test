package com.anibalbastias.android.pulentapp.module

import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.search.repository.SearchMusicRepositoryImpl
import com.anibalbastias.android.pulentapp.base.api.domain.search.repository.ISeriesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class PulentRepositoryModule {

    @Binds
    abstract fun bindSeriesDataRepository(repository: SearchMusicRepositoryImpl): ISeriesRepository

}