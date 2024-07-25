package com.example.playlistmaker.domain.interactors

import com.example.playlistmaker.domain.models.Track

interface HistoryInteractor {
     fun getTrack(): List<Track>
    fun saveTrackHistory(track: List<Track>): List<Track>
    fun clearHistory()
    fun checkHistory (track: Track):List<Track>
}