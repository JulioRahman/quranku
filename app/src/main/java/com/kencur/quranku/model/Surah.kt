package com.kencur.quranku.model

import org.json.JSONObject

data class Surah(
    val index: Int,
    val arabic: String,
    val latin: String,
    val translation: String,
    val ayahCount: Int
) {
    companion object {
        private fun parseJsonToSurah(json: JSONObject): Surah =
            Surah(
                json.getInt("index"),
                json.getString("arabic"),
                json.getString("latin"),
                json.getString("translation"),
                json.getInt("ayah_count")
            )

        fun parseJsonToList(json: JSONObject): List<Surah> {
            val listSurah = ArrayList<Surah>()
            val surahArray = json.getJSONArray("surah_info")

            for (i in 0 until surahArray.length()) {
                listSurah.add(parseJsonToSurah(surahArray.getJSONObject(i)))
            }

            return listSurah
        }
    }
}
