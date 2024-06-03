package com.example.playlistmaker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val context: Context) {

    private val sharedPreferences = context.getSharedPreferences(HISTORY_NAME, Context.MODE_PRIVATE)
    fun getTrack(): List<Track> {
        val json = sharedPreferences.getString(KEY_HISTORY, null) ?: return  listOf<Track>().toMutableList()
            val itemType = object : TypeToken<ArrayList<Track>>() {}.type
            return Gson().fromJson(json,itemType)

        }
    private fun saveTrackHistory (track: List<Track>):List<Track> {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(KEY_HISTORY,json)
            .apply()
        return track
    }
    fun checkHistory (track: Track):List<Track>{
        val historyItem = getTrack().toMutableList()
        historyItem.removeIf{it.trackId == track.trackId}
        historyItem.add(0,track)
        if (historyItem.size > 10) {
            historyItem.removeAt(historyItem.size-1)
        }
        val history = saveTrackHistory(historyItem)
    return history
    }
    fun clearHistory(){
            sharedPreferences.edit()
            .clear()
            .apply()
    }
    companion object {
        const val HISTORY_NAME = "histori_name"
        const val KEY_HISTORY= "items"
    }
}
