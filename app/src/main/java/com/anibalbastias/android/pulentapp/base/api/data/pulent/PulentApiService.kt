package com.anibalbastias.android.pulentapp.base.api.data.pulent


import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.common.search.SearchMusicData
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.QueryMap

/**
 * Created by anibalbastias on 3/03/19.
 */

interface PulentApiService {

    //Get Search Data
    @GET("search")
    fun searchMusic(@QueryMap parameters: Map<String, String>): Flowable<SearchMusicData>
}