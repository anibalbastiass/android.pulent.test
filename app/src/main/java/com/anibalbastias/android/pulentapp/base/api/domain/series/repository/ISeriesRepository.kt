package com.anibalbastias.android.pulentapp.base.api.domain.series.repository

import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.common.search.SearchMusicData
import io.reactivex.Flowable

/**
 * Created by anibalbastias on 3/19/18.
 */
interface ISeriesRepository {

    fun searchMusic(map: Map<String, String>): Flowable<SearchMusicData>
}