package com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.search.repository

import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.search.model.SearchMusicData
import com.anibalbastias.android.pulentapp.base.api.data.pulent.PulentApiService
import com.anibalbastias.android.pulentapp.base.api.domain.search.repository.ISeriesRepository
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by anibalbastias on 19-03-18.
 */
@Singleton
class SearchMusicRepositoryImpl @Inject constructor(private val pulentApiService: PulentApiService) : ISeriesRepository {

    override fun searchMusic(map: Map<String, String>): Flowable<SearchMusicData> =
        pulentApiService.searchMusic(map)

}