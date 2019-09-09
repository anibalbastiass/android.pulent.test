package com.anibalbastias.android.pulentapp.domain.search.repository

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.SearchMusicData
import io.reactivex.Flowable

/**
 * Created by anibalbastias on 3/19/18.
 */
interface ISeriesRepository {

    fun searchMusic(url: String): Flowable<SearchMusicData>
}