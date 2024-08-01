package com.example.playlistmaker.domain.interactors

import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.repositories.SearchHistoryRepositoryImpl
import com.example.playlistmaker.domain.api.HistoryInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repositories.SearchHistoryRepository

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


