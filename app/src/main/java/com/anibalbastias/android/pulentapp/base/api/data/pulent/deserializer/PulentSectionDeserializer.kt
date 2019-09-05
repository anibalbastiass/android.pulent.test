package com.anibalbastias.android.pulentapp.base.api.data.pulent.deserializer

import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.common.section.SectionData
import com.anibalbastias.android.pulentapp.base.api.data.dataStoreFactory.series.model.SeriesItemData
import com.anibalbastias.android.pulentapp.base.api.data.pulent.PulentAPIGSONManager
import com.anibalbastias.android.pulentapp.util.etagPage
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class PulentSectionDeserializer : JsonDeserializer<SectionData> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): SectionData? {

        val ret: SectionData?

        when (etagPage) {
            PulentAPIGSONManager.SECTION_ETAG_SERIES_DEFAULT -> {
                ret = context?.deserialize<SeriesItemData>(
                    json,
                    SeriesItemData::class.java
                )
            }
            else -> {
                ret = SectionData()
                ret.etag = etagPage
            }
            //endregion
        }

        return ret
    }

}