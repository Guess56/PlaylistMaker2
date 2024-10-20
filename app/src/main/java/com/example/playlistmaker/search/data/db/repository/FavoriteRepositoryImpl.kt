package com.example.playlistmaker.search.data.db.repository

import com.example.playlistmaker.search.data.converters.TrackDbConverter
import com.example.playlistmaker.search.data.db.AppDataBase
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.db.repository.FavoriteRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoriteRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val trackDbConverter: TrackDbConverter,
): FavoriteRepository {
    override suspend fun insertFavoriteTrack(track:List<TrackEntity>) {
        val tracks = appDataBase.trackDao().insertTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track:TrackEntity) {
        val tracks = appDataBase.trackDao().deleteTrackEntity(track)
    }

    override suspend fun getFavoriteTrack(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks:List<TrackEntity>): List<Track>{
        return tracks.map { track -> trackDbConverter.map(track)}
    }
}
