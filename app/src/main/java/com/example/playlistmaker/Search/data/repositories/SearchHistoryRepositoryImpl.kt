package com.example.playlistmaker.Search.data.repositories

import android.content.SharedPreferences
import com.example.playlistmaker.Search.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.Search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(private val sharedPreferences:SharedPreferences) :
    SearchHistoryRepository {

    companion object {
        const val KEY_HISTORY = "items"
        const val max_item = 10
    }

    override fun getTrack(): List<Track> {
        val json =
            sharedPreferences.getString(KEY_HISTORY, null) ?: return listOf<Track>().toMutableList()
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, itemType)
    }

     override fun saveTrackHistory(track: List<Track>): List<Track> {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(KEY_HISTORY, json)
            .apply()
        return track
    }
    override fun clearHistory(){
        sharedPreferences.edit()
            .clear()
            .apply()
    }
    override fun checkHistory (track: Track):List<Track>{
        val historyItem = getTrack().toMutableList()
        historyItem.removeIf{it.trackId == track.trackId}
        historyItem.add(0,track)
        if (historyItem.size > max_item) {
            historyItem.removeAt(historyItem.size-1)
        }
        val history = saveTrackHistory(historyItem)
        return history
    }
}