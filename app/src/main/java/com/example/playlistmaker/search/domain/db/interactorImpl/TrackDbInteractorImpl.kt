package com.example.playlistmaker.search.domain.db.interactorImpl

import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.db.interactor.TrackDbInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.domain.db.repository.TrackDbRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackDbInteractorImpl(private val repository: TrackDbRepository): TrackDbInteractor {
    override suspend fun insertTrack(track: List<TrackEntity>) {
        repository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: TrackEntity) {
        repository.deleteTrack(track)
    }

    override suspend fun getTrack(): Flow<List<Track>> {
        return repository.getTrack()
    }

    override suspend fun getTrackId(track:Long): Flow<TrackEntity> {
        return repository.getTrackId(track)
    }

    override suspend fun updateTrack(track: TrackEntity) {
        return repository.updateTrack(track)
    }


}