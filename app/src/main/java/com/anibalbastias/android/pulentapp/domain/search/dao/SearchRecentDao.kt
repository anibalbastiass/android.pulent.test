package com.anibalbastias.android.pulentapp.domain.search.dao

import com.anibalbastias.android.pulentapp.domain.search.model.SearchRecentRealmData
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults


/**
 * Created by anibalbastias on 2019-09-07.
 */

class SearchRecentDao(realm: Realm?) {

    private lateinit var mRealm: Realm

    init {
        realm?.let {
            mRealm = it
        }
    }

    fun save(user: SearchRecentRealmData) {
        mRealm.executeTransaction { realm -> realm.copyToRealmOrUpdate(user) }
    }

    fun save(userList: List<SearchRecentRealmData>) {
        mRealm.executeTransaction { realm -> realm.copyToRealmOrUpdate(userList) }
    }

    fun loadAll(): RealmResults<SearchRecentRealmData> {
        return mRealm.where(SearchRecentRealmData::class.java).findAll()
    }

    fun loadAllAsync(): RealmResults<SearchRecentRealmData> {
        return mRealm.where(SearchRecentRealmData::class.java).findAllAsync()
    }

    fun loadBy(id: Long): RealmObject {
        return mRealm.where(SearchRecentRealmData::class.java).equalTo("id", id).findFirst()!!
    }

    fun remove(item: RealmObject?) {
        mRealm.executeTransaction { item?.deleteFromRealm() }
    }

    fun removeAll() {
        mRealm.executeTransaction { mRealm.delete(SearchRecentRealmData::class.java) }
    }

    fun count(): Long {
        return mRealm.where(SearchRecentRealmData::class.java).count()
    }
}