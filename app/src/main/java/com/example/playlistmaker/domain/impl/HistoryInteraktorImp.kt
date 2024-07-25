package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.interactors.HistoryInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repositories.SearchHistoryRepository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryInteraktorImp(private val repository : SearchHistoryRepository):HistoryInteractor {

    val sharedPreferences = Creator.provideSharedPreferences(SearchHistoryRepositoryImpl.HISTORY_NAME)

    override fun getTrack(): List<Track> {
        val json =
            sharedPreferences.getString(SearchHistoryRepositoryImpl.KEY_HISTORY, null) ?: return listOf<Track>().toMutableList()
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, itemType)
    }
    override fun saveTrackHistory(track: List<Track>): List<Track> {
        val json = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(SearchHistoryRepositoryImpl.KEY_HISTORY, json)
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
        if (historyItem.size > SearchHistoryRepositoryImpl.max_item) {
            historyItem.removeAt(historyItem.size-1)
        }
        val history = saveTrackHistory(historyItem)
        return history
    }
}


