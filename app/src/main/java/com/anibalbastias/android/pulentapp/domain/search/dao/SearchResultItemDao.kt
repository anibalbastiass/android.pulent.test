package com.anibalbastias.android.pulentapp.domain.search.dao

import com.anibalbastias.android.pulentapp.domain.search.model.ResultItemRealmData
import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmResults


/**
 * Created by anibalbastias on 2019-09-07.
 */

class SearchResultItemDao(realm: Realm?) {

    private lateinit var mRealm: Realm

    init {
        realm?.let {
            mRealm = it
        }
    }

    fun save(user: ResultItemRealmData) {
        mRealm.executeTransaction { realm -> realm.copyToRealmOrUpdate(user) }
    }

    fun save(userList: List<ResultItemRealmData>) {
        mRealm.executeTransaction { realm -> realm.copyToRealmOrUpdate(userList) }
    }

    fun loadAll(): RealmResults<ResultItemRealmData> {
        return mRealm.where(ResultItemRealmData::class.java).findAll()
    }

    fun loadAllAsync(): RealmResults<ResultItemRealmData> {
        return mRealm.where(ResultItemRealmData::class.java).findAllAsync()
    }

    fun loadBy(id: Long): RealmObject {
        return mRealm.where(ResultItemRealmData::class.java).equalTo("trackId", id).findFirst()!!
    }

    fun remove(item: RealmObject?) {
        mRealm.executeTransaction { item?.deleteFromRealm() }
    }

    fun removeAll() {
        mRealm.executeTransaction { mRealm.delete(ResultItemRealmData::class.java) }
    }

    fun count(): Long {
        return mRealm.where(ResultItemRealmData::class.java).count()
    }
}