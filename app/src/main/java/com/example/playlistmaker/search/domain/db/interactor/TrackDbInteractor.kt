package com.example.playlistmaker.search.domain.db.interactor

import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackDbInteractor {
    suspend fun insertTrack(track:List<TrackEntity>)
    suspend fun deleteTrack(track: TrackEntity)
    suspend fun getTrack(): Flow<List<Track>>
    suspend fun getTrackId(track:Long):Flow<TrackEntity>
    suspend fun updateTrack(track: TrackEntity)

}