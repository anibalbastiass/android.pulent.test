package com.anibalbastias.android.pulentapp.ui.search.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by anibalbastias on 2019-09-07.
 */

@Parcelize
data class SearchMusicViewData(
    val resultCount: Int? = null,
    val results: ArrayList<SearchResultItemViewData?>? = null
) : Parcelable