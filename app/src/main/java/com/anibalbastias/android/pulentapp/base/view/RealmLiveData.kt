package com.anibalbastias.android.pulentapp.base.view

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Created by anibalbastias on 2019-09-08.
 */

class RealmLiveData<T : RealmModel>(val realmResults: RealmResults<T>) :
    LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> value = results }

    override fun onActive() {
        realmResults.addChangeListener(listener)
    }

    override fun onInactive() {
        realmResults.removeChangeListener(listener)
    }
}