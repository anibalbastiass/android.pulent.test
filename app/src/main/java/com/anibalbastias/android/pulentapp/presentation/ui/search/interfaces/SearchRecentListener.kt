package com.anibalbastias.android.pulentapp.presentation.ui.search.interfaces

import android.view.View
import com.anibalbastias.android.pulentapp.domain.search.model.SearchRecentRealmData
import com.anibalbastias.android.pulentapp.presentation.util.adapter.base.BaseBindClickHandler
import io.realm.RealmResults

/**
 * Created by anibalbastias on 2019-08-13.
 */

interface SearchRecentListener<T> : BaseBindClickHandler<T> {

    override fun onClickView(view: View, item: T)
    fun onClickSearchRecentItem(list: SearchRecentRealmData)
    fun onClearSearchItems()
}