package com.anibalbastias.android.pulentapp.domain.base.model

/**
 * Created by anibalbastias on 2019-09-07.
 */

import io.realm.RealmObject

open class RealmString : RealmObject {

    var string: String? = null

    constructor() {}

    constructor(str: String) {
        string = str
    }
}