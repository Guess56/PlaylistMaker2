package com.example.playlistmaker.favorite.data.db.repository

import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.favorite.data.db.converter.FavoriteTrackDbConverter
import com.example.playlistmaker.favorite.data.db.entity.FavoriteEntity
import com.example.playlistmaker.favorite.domain.repository.FavoriteRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoriteRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val favoriteTrackDbConverter: FavoriteTrackDbConverter,
): FavoriteRepository {
    override suspend fun insertFavoriteTrack(track:FavoriteEntity) {
        val tracks = appDataBase.favoriteDao().insertTrack(track)
    }

    override suspend fun deleteFavoriteTrack(track:FavoriteEntity) {
        val tracks = appDataBase.favoriteDao().deleteTrackEntity(track)

    }

    override suspend fun getFavoriteTrack(): Flow<List<Track>> = flow {
        val tracks = appDataBase.favoriteDao().sortTrack()

        emit(convertFromFavoriteEntity(tracks))
    }


    override suspend fun getFavoriteTrackId(): Flow<List<Long>> = flow {
        val tracks =appDataBase.favoriteDao().getTracksIds()
        emit(tracks)
    }



    private fun convertFromFavoriteEntity(tracks:List<FavoriteEntity>): List<Track>{
        return tracks.map { track -> favoriteTrackDbConverter.map(track)}
    }
}
