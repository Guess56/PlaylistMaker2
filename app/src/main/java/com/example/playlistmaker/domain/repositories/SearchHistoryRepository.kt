package com.example.playlistmaker.domain.repositories

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
     fun getTrack(): List<Track>
    fun saveTrackHistory(track: List<Track>): List<Track>
    fun clearHistory()
    fun checkHistory (track: Track):List<Track>

}