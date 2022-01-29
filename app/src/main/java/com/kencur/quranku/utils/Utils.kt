package com.kencur.quranku.utils

import android.content.Context
import com.kencur.quranku.model.Surah
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

fun getJsonDataFromAsset(context: Context, fileName: String): JSONObject? {
    val jsonString: String
    val jsonObject: JSONObject

    try {
        jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        return null
    }

    try {
        jsonObject = JSONObject(jsonString)
    } catch (jsonException: JSONException) {
        jsonException.printStackTrace()
        return null
    }

    return jsonObject
}

fun getListSurah(context: Context): List<Surah>? {
    return getJsonDataFromAsset(context, "surah_info.json")?.let {
        Surah.parseJsonToList(it)
    }
}

