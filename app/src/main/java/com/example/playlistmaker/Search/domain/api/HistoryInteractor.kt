package com.example.playlistmaker.Search.domain.api

import com.example.playlistmaker.Search.domain.models.Track

interface HistoryInteractor {
    fun getTrack(): List<Track>
    fun saveTrackHistory(track: List<Track>): List<Track>
    fun clearHistory()
    fun checkHistory (track: Track):List<Track>
}