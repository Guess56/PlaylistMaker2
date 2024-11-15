package com.example.playlistmaker.search.data.repositories

import android.util.Log
import com.example.playlistmaker.search.data.converters.TrackDbConverter
import com.example.playlistmaker.AppDataBase
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.data.network.NetworkClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDataBase: AppDataBase,
    private val trackDbConverter: TrackDbConverter) : TrackRepository {
    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when(response.resultCode){
            200 -> {
                with(response as TrackResponse) {
                    val trackList = results.map {trackDto ->

                        Track(

                            trackId = trackDto.trackId,
                            trackName = trackDto.trackName,
                            artistName = trackDto.artistName,
                            trackTimeMillis = trackDto.trackTimeMillis,
                            artworkUrl100 = trackDto.artworkUrl100,
                            collectionName = trackDto.collectionName,
                            releaseDate = trackDto.releaseDate,
                            primaryGenreName = trackDto.primaryGenreName,
                            country = trackDto.country,
                            previewUrl = trackDto.previewUrl,
                            inFavorite = trackDto.inFavorite,
                            timeAdd = System.currentTimeMillis()
                        )}


                        val list = appDataBase.favoriteDao().getTracksIds()

                        for (i in trackList){
                        if(i.trackId.toLong() in list){
                            i.inFavorite = true
                        } else {
                            i.inFavorite = false
                        }
                    }


                    saveTrack(trackList)

                    emit(Resource.Success(trackList))
                }
            }
            else -> {
                emit(Resource.Error(response.resultCode))
            }
        }

    }
    private suspend fun saveTrack(tracks: List<Track>) {
        val trackEntities = tracks.map { track -> trackDbConverter.map(track)}
        appDataBase.trackDao().insertTrack(trackEntities)
    }


}

