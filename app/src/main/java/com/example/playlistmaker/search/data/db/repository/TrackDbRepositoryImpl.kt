package com.example.playlistmaker.search.data.db.repository

import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.search.data.converters.TrackDbConverter
import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.db.repository.TrackDbRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackDbRepositoryImpl (private val appDataBase: AppDataBase,
                             private val trackDbConverter: TrackDbConverter
): TrackDbRepository {
    override suspend fun insertTrack(track:List<TrackEntity>) {
        val tracks = appDataBase.trackDao().insertTrack(track)
    }

    override suspend fun deleteTrack(track: TrackEntity) {
        val tracks = appDataBase.trackDao().deleteTrackEntity(track)
    }

    override suspend fun getTrack(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun getTrackId(track: Long): Flow<TrackEntity> = flow{
        val tracks = appDataBase.trackDao().getTrackById(track)
        emit(tracks)
    }

    override suspend fun getTrackIds(track: Long): TrackEntity {
        return appDataBase.trackDao().getTrackById(track)
    }

    override suspend fun updateTrack(track: TrackEntity) {
        val tracks = appDataBase.trackDao().updateTrackEntity(track)
    }


    private fun convertFromTrackEntity(tracks:List<TrackEntity>): List<Track>{
        return tracks.map { track -> trackDbConverter.map(track)}
    }

}
