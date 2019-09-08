package com.anibalbastias.android.pulentapp.base.api.domain.base.db

import com.anibalbastias.android.pulentapp.base.api.domain.search.dao.SearchResultItemDao
import com.anibalbastias.android.pulentapp.base.api.domain.search.model.ResultItemRealmData
import io.realm.Realm
import io.realm.kotlin.delete


/**
 * Created by anibalbastias on 2019-09-07.
 */

object RealmManager {

    private var mRealm: Realm? = null

    fun open(): Realm? {
        mRealm = Realm.getDefaultInstance()
        return mRealm
    }

    fun close() {
        if (mRealm != null) {
            mRealm!!.close()
        }
    }

    fun createSearchResultItemDao(): SearchResultItemDao {
        checkForOpenRealm()
        return SearchResultItemDao(mRealm)
    }

    fun clear() {
        checkForOpenRealm()
        mRealm?.executeTransaction { realm ->
            realm.delete<ResultItemRealmData>()
//            realm.clear(ResultItemRealmData::class.java)
            //clear rest of your dao classes
        }
    }

    private fun checkForOpenRealm() {
        check(!(mRealm == null || mRealm!!.isClosed())) { "RealmManager: Realm is closed, call open() method first" }
    }
}