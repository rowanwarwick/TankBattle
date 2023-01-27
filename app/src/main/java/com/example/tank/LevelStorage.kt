package com.example.tank

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.example.tank.models.Element
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LevelStorage(val activity: Activity) {
    val prefs = activity.getPreferences(MODE_PRIVATE)
    fun saveLevel(elementContainer: List<Element>) {
        prefs.edit().putString("key_level", Gson().toJson(elementContainer)).apply()
    }

    fun loadLevel():List<Element>? {
        val level = prefs.getString("key_level", null)
        level?.let {
            val type = object :TypeToken<List<Element>>(){}.type
            return Gson().fromJson(it, type)
        }
        return null
    }
}