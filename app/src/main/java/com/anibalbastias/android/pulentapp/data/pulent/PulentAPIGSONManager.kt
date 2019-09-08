package com.anibalbastias.android.pulentapp.data.pulent

import com.anibalbastias.android.pulentapp.data.dataStoreFactory.common.WrapperData
import com.anibalbastias.android.pulentapp.data.pulent.deserializer.PulentWrapperDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class PulentAPIGSONManager private constructor() {

    @Retention(AnnotationRetention.SOURCE)
    annotation class SectionType

    @Retention(AnnotationRetention.SOURCE)
    annotation class ActionType

    private fun createGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    private fun createGsonBuilder(): GsonBuilder {
        val gsonBuilder = GsonBuilder()

        gsonBuilder.registerTypeAdapter(WrapperData::class.java, PulentWrapperDeserializer())

        return gsonBuilder
    }

    companion object {
        private var instance: PulentAPIGSONManager? = null

        //region Wrapper
        const val COLLECTION_WRAPPER_TYPE = "collection"
        const val TRACK_WRAPPER_TYPE = "track"
        //endregion

        fun create(): Gson {
            if (instance == null) {
                instance = PulentAPIGSONManager()
            }
            return instance!!.createGson()
        }

        fun createGsonBuilder(): GsonBuilder {
            if (instance == null) {
                instance = PulentAPIGSONManager()
            }
            return instance!!.createGsonBuilder()
        }
    }
}