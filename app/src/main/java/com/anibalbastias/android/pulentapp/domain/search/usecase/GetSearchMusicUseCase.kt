package com.anibalbastias.android.pulentapp.domain.search.usecase

import au.com.carsales.apibaselib.domain.interactor.FlowableUseCase
import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.SearchMusicData
import com.anibalbastias.android.pulentapp.domain.base.executor.APIPostExecutionThread
import com.anibalbastias.android.pulentapp.domain.base.executor.APIThreadExecutor
import com.anibalbastias.android.pulentapp.domain.search.repository.ISeriesRepository
import io.reactivex.Flowable
import javax.inject.Inject

class GetSearchMusicUseCase @Inject constructor(
    private val detailRepository: ISeriesRepository,
    threadExecutor: APIThreadExecutor,
    postExecutionThread: APIPostExecutionThread
) : FlowableUseCase<SearchMusicData, String?>(threadExecutor, postExecutionThread) {

    override fun buildUseCaseObservable(params: String?): Flowable<SearchMusicData> {
        return params?.let { detailRepository.searchMusic(it) }!!
    }
}