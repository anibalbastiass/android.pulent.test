package com.anibalbastias.android.pulentapp.data.pulent.deserializer

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.common.WrapperData
import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.CollectionResultItemData
import com.anibalbastias.android.pulentapp.data.dataStoreFactory.search.model.TrackResultItemData
import com.anibalbastias.android.pulentapp.data.pulent.PulentAPIGSONManager
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class PulentWrapperDeserializer : JsonDeserializer<WrapperData> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): WrapperData? {

        var ret: WrapperData? = null
        val type = json?.asJsonObject?.get("wrapperType")?.asString

        when (type) {
            PulentAPIGSONManager.TRACK_WRAPPER_TYPE -> {
                ret = context?.deserialize<TrackResultItemData>(json,
                    TrackResultItemData::class.java)
            }
            PulentAPIGSONManager.COLLECTION_WRAPPER_TYPE -> {
                ret = context?.deserialize<CollectionResultItemData>(
                    json,
                    CollectionResultItemData::class.java
                )
            }
            else -> {

            }
            //endregion
        }

        return ret
    }

}