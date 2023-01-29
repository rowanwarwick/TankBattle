package com.example.tank

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import com.example.tank.models.Element
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LevelStorage(activity: Activity) {
    val prefs = activity.getPreferences(MODE_PRIVATE)
    val gson = Gson()
    fun saveLevel(elementContainer: List<Element>) {
        prefs.edit().putString("key_level", gson.toJson(elementContainer)).apply()
    }

    fun loadLevel():List<Element>? {
        val level = prefs.getString("key_level", null) ?: return null
        val type = object :TypeToken<List<Element>>(){}.type
        return gson.fromJson(level, type)
    }
}