package com.example.playlistmaker.favorite.domain.interactor

import com.example.playlistmaker.favorite.data.db.entity.FavoriteEntity
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    suspend fun insertFavoriteTrack(track:FavoriteEntity)
    suspend fun deleteFavoriteTrack(track:FavoriteEntity)
    suspend fun getFavoriteTrack(): Flow<List<Track>>
    suspend fun getFavoriteTrackId():Flow<List<Long>>


}