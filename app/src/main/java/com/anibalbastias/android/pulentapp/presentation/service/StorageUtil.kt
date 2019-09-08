package com.anibalbastias.android.pulentapp.presentation.service

import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE
import com.anibalbastias.android.pulentapp.presentation.service.model.AudioData
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


/**
 * Created by anibalbastias on 2019-09-08.
 */

class StorageUtil(private val context: Context) {

    private val STORAGE = "com.anibalbastias.android.pulentapp.STORAGE"
    private var preferences: SharedPreferences? = null

    fun storeAudio(arrayList: ArrayList<AudioData>) {
        preferences = context.getSharedPreferences(STORAGE, MODE_PRIVATE)

        val editor = preferences!!.edit()
        val gson = Gson()
        val json = gson.toJson(arrayList)
        editor.putString("audioArrayList", json)
        editor.apply()
    }

    fun loadAudio(): ArrayList<AudioData> {
        preferences = context.getSharedPreferences(STORAGE, MODE_PRIVATE)
        val gson = Gson()
        val json = preferences!!.getString("audioArrayList", null)
        val type = object : TypeToken<ArrayList<AudioData>>() {

        }.type
        return gson.fromJson(json, type)
    }

    fun storeAudioIndex(index: Int) {
        preferences = context.getSharedPreferences(STORAGE, MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.putInt("audioIndex", index)
        editor.apply()
    }

    fun loadAudioIndex(): Int {
        preferences = context.getSharedPreferences(STORAGE, MODE_PRIVATE)
        return preferences!!.getInt("audioIndex", -1)//return -1 if no data found
    }

    fun clearCachedAudioPlaylist() {
        preferences = context.getSharedPreferences(STORAGE, MODE_PRIVATE)
        val editor = preferences!!.edit()
        editor.clear()
        editor.apply()
    }
}