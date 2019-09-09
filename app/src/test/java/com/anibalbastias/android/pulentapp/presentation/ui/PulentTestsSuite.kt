package com.anibalbastias.android.pulentapp.presentation.ui

import com.anibalbastias.android.pulentapp.presentation.ui.resultitem.viewmodel.ResultItemViewModelTest
import com.anibalbastias.android.pulentapp.presentation.ui.search.viewmodel.SearchMusicViewModelTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Created by anibalbastias on 02-09-2019.
 */

@RunWith(Suite::class)
@Suite.SuiteClasses(
        ResultItemViewModelTest::class,
        SearchMusicViewModelTest::class
)
class PulentTestsSuite {
    /**
     * The purpose of this test suite class is to run at once all the tests related to Test Drive
     * function and check their results without running each test by separated. To add new tests
     * just add them inside the "Suite Classes" annotation separated by a comma.
     */
}