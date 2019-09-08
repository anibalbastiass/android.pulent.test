package com.anibalbastias.android.pulentapp.domain.search.model

/**
 * Created by anibalbastias on 2019-09-07.
 */

import com.anibalbastias.android.pulentapp.domain.base.model.RealmString
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class ResultItemRealmData : RealmObject() {

    @PrimaryKey @JvmField
    var trackId: Int = 0
    var artistId: Int? = null
    var collectionId: Int? = null
    var kind: String? = null

    var trackName: String? = null
    var artworkUrl100: String? = null
    var trackTimeMillis: Int? = null
    var country: String? = null
    var previewUrl: String? = null
    var collectionName: String? = null
    var artistViewUrl: String? = null
    var discNumber: Int? = null
    var trackCount: Int? = null
    var artworkUrl30: String? = null
    var wrapperType: String? = null
    var currency: String? = null

    var isStreamable: Boolean? = null
    var trackExplicitness: String? = null
    var collectionViewUrl: String? = null
    var trackNumber: Int? = null
    var releaseDate: String? = null
    var collectionPrice: Double? = null
    var discCount: Int? = null
    var primaryGenreName: String? = null
    var trackPrice: Double? = null
    var collectionExplicitness: String? = null
    var trackViewUrl: String? = null
    var artworkUrl60: String? = null
    var trackCensoredName: String? = null
    var artistName: String? = null
    var collectionCensoredName: String? = null

    var trackList: RealmList<RealmString>? = null

    init {
        trackList = RealmList()
    }

    companion object {

        fun equals(userList1: List<ResultItemRealmData>?, userList2: List<ResultItemRealmData>?): Boolean {
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

        fun equals(user1: ResultItemRealmData, user2: ResultItemRealmData): Boolean {
            if (user1.trackId != user2.trackId) return false

            if (user1.artistId != user2.artistId) return false

            if (user1.kind == null || user2.kind == null || user1.kind != user2.kind)
                return false

            if (user1.collectionId == null || user2.collectionId == null || user1.collectionId != user2.collectionId)
                return false

            if (user1.trackList != null && user2.trackList != null && user1.trackList!!.size == user2.trackList!!.size) {
                for (i in 0 until user1.trackList!!.size) {
                    val contact1 = user1.trackList!![i]?.string
                    val contact2 = user2.trackList!![i]?.string

                    if (contact1 != contact2) {
                        return false
                    }
                }
            }

            return true
        }
    }
}