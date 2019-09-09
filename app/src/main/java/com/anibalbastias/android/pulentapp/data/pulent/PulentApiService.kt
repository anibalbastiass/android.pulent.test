package com.anibalbastias.android.pulentapp.data.pulent

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.SearchMusicData
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Created by anibalbastias on 3/03/19.
 */

interface PulentApiService {

    //Get Search Data
    @GET
    fun searchMusic(@Url nextUrl: String): Flowable<SearchMusicData>
}