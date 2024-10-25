package com.example.playlistmaker.search.domain.interactors

import com.example.playlistmaker.search.domain.api.HistoryInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.repositories.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow

class HistoryInteractorImp(private val repository : SearchHistoryRepository): HistoryInteractor {

    override fun getTrack(): List<Track> {
        return repository.getTrack()
    }

    override fun getTrackFlow(): Flow<List<Track>> {
        return repository.getTrackFlow()
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


