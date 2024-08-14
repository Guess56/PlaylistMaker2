package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun getTrack(): List<Track>
    fun saveTrackHistory(track: List<Track>): List<Track>
    fun clearHistory()
    fun checkHistory (track: Track):List<Track>
}