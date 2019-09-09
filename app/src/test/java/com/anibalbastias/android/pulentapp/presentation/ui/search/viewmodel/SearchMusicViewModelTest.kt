package com.anibalbastias.android.pulentapp.presentation.ui.search.viewmodel

import au.com.carsales.autogate.BaseUnitTest
import com.anibalbastias.android.pulentapp.base.subscriber.BaseSubscriber
import com.anibalbastias.android.pulentapp.base.view.ResourceState
import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.SearchMusicData
import com.anibalbastias.android.pulentapp.domain.search.usecase.GetSearchMusicUseCase
import com.anibalbastias.android.pulentapp.presentation.BaseDataManager.Companion.getErrorMessage
import com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.SearchViewDataMapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.mapper.realm.SearchResultItemRealmMapper
import com.anibalbastias.android.pulentapp.presentation.ui.search.model.SearchMusicViewData
import com.nhaarman.mockito_kotlin.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito

/**
 * Created by anibalbastias on 2019-09-09.
 */

class SearchMusicViewModelTest : BaseUnitTest() {

    override fun showErrorMessage(): Boolean = false

    override fun initCaptors() {
        captorSearchMusic = argumentCaptor()
    }

    override fun initDataMocks() {
        getSearchMusicUseCase = mock()
        searchViewDataMapper = mock()
        searchResultItemRealmMapper = mock()
    }

    override fun initViewModel() {
        searchMusicViewModel = SearchMusicViewModel(
            getSearchMusicUseCase,
            searchViewDataMapper,
            searchResultItemRealmMapper
        )
    }

    // region Mocked vars
    @Mock
    lateinit var getSearchMusicUseCase: GetSearchMusicUseCase
    @Mock
    lateinit var searchViewDataMapper: SearchViewDataMapper
    @Mock
    lateinit var searchResultItemRealmMapper: SearchResultItemRealmMapper
    // endregion

    // region Captors vars
    @Captor
    private lateinit var captorSearchMusic: KArgumentCaptor<BaseSubscriber<SearchMusicViewData, SearchMusicData>>
    // endregion

    private lateinit var searchMusicViewModel: SearchMusicViewModel

    // region Setup Data

    @Before
    fun setUp() {
        initCaptors()
        initDataMocks()
        initViewModel()
    }
    // endregion

    // region Use Case Execute Testing

    @Test
    fun testSearchAlbumsResultsUseCase() {
        searchMusicViewModel.getSearchAlbumsResultsData(true)

        Mockito.verify(getSearchMusicUseCase, times(1)).execute(any(), anyOrNull())
    }
    // endregion

    // region LiveData Status Test Drive Check-In

    @Test
    fun testStatusLiveDataTestDriveCompleteItem_isLoading() {
        searchMusicViewModel.let { viewModel ->
            searchMusicViewModel.getSearchAlbumsResultsData(false)

            Mockito.verify(getSearchMusicUseCase, times(1))
                .execute(captorSearchMusic.capture(), anyOrNull())

            assertEquals(true, viewModel.isLoading.get())
            assertEquals(false, viewModel.isError.get())
            assertNotNull(viewModel.getSearchResultsLiveData().value)
            assertEquals(ResourceState.LOADING, viewModel.getSearchResultsLiveData().value?.status)
        }
    }

    @Test
    fun testStatusLiveDataTestDriveCompleteItem_isError() {
        val methodName = "${object : Any() {}.javaClass.enclosingMethod?.name}()"

        searchMusicViewModel.let { viewModel ->
            searchMusicViewModel.getSearchAlbumsResultsData(true)

            captorSearchMusic.let {
                Mockito.verify(getSearchMusicUseCase, times(1))
                    .execute(it.capture(), anyOrNull())
                it.firstValue.onComplete()
                it.firstValue.onError(getErrorMessage(showErrorMessage(), methodName))
            }

            assertEquals(false, viewModel.isLoading.get())
            assertEquals(true, viewModel.isError.get())
            assertNotNull(viewModel.getSearchResultsLiveData().value)
            assertEquals(ResourceState.ERROR, viewModel.getSearchResultsLiveData().value?.status)
        }
    }
    // endregion
}