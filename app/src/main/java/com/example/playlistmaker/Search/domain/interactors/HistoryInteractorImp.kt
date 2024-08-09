package com.example.playlistmaker.Search.domain.interactors

import com.example.playlistmaker.Search.domain.api.HistoryInteractor
import com.example.playlistmaker.Search.domain.models.Track
import com.example.playlistmaker.Search.domain.repositories.SearchHistoryRepository

class HistoryInteractorImp(private val repository : SearchHistoryRepository): HistoryInteractor {

    override fun getTrack(): List<Track> {
        return repository.getTrack()
    }
    override fun saveTrackHistory(track: List<Track>): List<Track> {
        return repository.saveTrackHistory(track)
    }
    override fun clearHistory(){
        repository.clearHistory()
    }
    override fun checkHistory (track: Track):List<Track>{
        return repository.checkHistory(track)
    }
}


