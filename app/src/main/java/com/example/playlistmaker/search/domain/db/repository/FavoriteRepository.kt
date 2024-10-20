package com.example.playlistmaker.search.domain.db.repository

import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow


interface FavoriteRepository {
    suspend fun insertFavoriteTrack(track:List<TrackEntity>)
    suspend fun deleteFavoriteTrack(track:TrackEntity)
    suspend fun getFavoriteTrack(): Flow<List<Track>>
}