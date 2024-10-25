package com.example.playlistmaker.favorite.domain.interactorimpl

import com.example.playlistmaker.favorite.data.db.entity.FavoriteEntity
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.favorite.domain.interactor.FavoriteInteractor
import com.example.playlistmaker.favorite.domain.repository.FavoriteRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(private val repository: FavoriteRepository): FavoriteInteractor {
    override suspend fun insertFavoriteTrack(track:FavoriteEntity) {
        return repository.insertFavoriteTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track:FavoriteEntity) {
        return repository.deleteFavoriteTrack(track)
    }

    override suspend fun getFavoriteTrack(): Flow<List<Track>> {
        return repository.getFavoriteTrack()
    }

    override suspend fun getFavoriteTrackId(): Flow<List<Long>> {
        return repository.getFavoriteTrackId()
    }



}