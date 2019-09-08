package com.anibalbastias.android.pulentapp.domain.search.model

import com.anibalbastias.android.pulentapp.domain.base.model.RealmString
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by anibalbastias on 2019-09-08.
 */

open class SearchRecentRealmData  : RealmObject() {

    @PrimaryKey @JvmField
    var id: Int = 0
    var keyword: String? = null
    var results: RealmList<ResultItemRealmData?>? = null

    var searchList: RealmList<RealmString>? = null

    init {
        searchList = RealmList()
    }

    companion object {

        fun equals(userList1: List<SearchRecentRealmData>?, userList2: List<SearchRecentRealmData>?): Boolean {
            var isEquals = true

            if (userList1 != null && userList2 != null && userList1.size == userList2.size) {
                for (i in userList1.indices) {
                    var user1 = userList1[i]
                    var user2 = userList2[i]

                    if (!equals(user1, user2)) {
                        isEquals = false
                        break
                    }
                }
            } else {
                isEquals = false
            }

            return isEquals
        }

        fun equals(user1: SearchRecentRealmData, user2: SearchRecentRealmData): Boolean {
            if (user1.id != user2.id) return false

            if (user1.keyword != user2.keyword) return false

            if (user1.results == null || user2.results == null || user1.results != user2.results)
                return false

            if (user1.searchList != null && user2.searchList != null && user1.searchList!!.size == user2.searchList!!.size) {
                for (i in 0 until user1.searchList!!.size) {
                    val contact1 = user1.searchList!![i]?.string
                    val contact2 = user2.searchList!![i]?.string

                    if (contact1 != contact2) {
                        return false
                    }
                }
            }

            return true
        }
    }
}