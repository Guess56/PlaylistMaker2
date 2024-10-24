package com.example.playlistmaker.search.domain.repositories

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun getTrack(): List<Track>
    fun getTrackFlow(): Flow<List<Track>>
    fun saveTrackHistory(track: List<Track>): List<Track>
    fun clearHistory()
    fun checkHistory(track: Track): List<Track>
    

}