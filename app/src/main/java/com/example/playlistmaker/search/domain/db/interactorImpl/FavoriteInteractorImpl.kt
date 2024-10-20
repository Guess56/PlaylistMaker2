package com.example.playlistmaker.search.domain.interactors

import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.db.repositories.FavoriteInteractor
import com.example.playlistmaker.search.domain.db.repository.FavoriteRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val repository: FavoriteRepository):FavoriteInteractor {
    override suspend fun insertFavoriteTrack(track:List<TrackEntity>) {
        return repository.insertFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track:TrackEntity) {
        return repository.deleteFavoriteTrack(track)
    }

    override suspend fun getFavoriteTrack(): Flow<List<Track>> {
        return repository.getFavoriteTrack()
    }
}