package com.anibalbastias.android.pulentapp.base.api.domain.search.repository

import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.search.model.SearchMusicData
import io.reactivex.Flowable

/**
 * Created by anibalbastias on 3/19/18.
 */
interface ISeriesRepository {

    fun searchMusic(url: String): Flowable<SearchMusicData>
}