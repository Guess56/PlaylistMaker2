package com.example.playlistmaker.search.data.repositories

import android.content.SharedPreferences
import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.search.domain.repositories.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class SearchHistoryRepositoryImpl(private val sharedPreferences:SharedPreferences,private val appDataBase: AppDataBase) :
    SearchHistoryRepository {

    companion object {
        const val KEY_HISTORY = "items"
        const val max_item = 10
    }
    var trackList = mutableListOf<Track>()

    override fun getTrack(): List<Track> {
        val json =
            sharedPreferences.getString(KEY_HISTORY, null) ?: return listOf<Track>().toMutableList()
        val itemType = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, itemType)
    }

    override fun getTrackFlow(): Flow<List<Track>> =flow{
        val trackHistory = getTrack()
        val favoriteList = appDataBase.favoriteDao().getTracksIds()
        for(i in trackHistory){
            if(i.trackId.toLong() in favoriteList ){
                i.inFavorite = true
            }
        }
        val trackList = trackHistory.map { track ->
            Track(trackId = track.trackId,
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl,
                inFavorite = track.inFavorite
                )
        }
        setList(trackList)
        emit(trackList)
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
        //val historyItem = getTrack().toMutableList()
        val historyItem=trackList
        historyItem.removeIf{it.trackId == track.trackId}
        historyItem.add(0,track)
        if (historyItem.size > max_item) {
            historyItem.removeAt(historyItem.size-1)
        }
        val history = saveTrackHistory(historyItem)
        return history
    }

    fun setList(track:List<Track>){
        trackList = track.toMutableList()
    }

}
