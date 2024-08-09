package com.example.playlistmaker.Search.data.repositories

import com.example.playlistmaker.Search.data.dto.TrackResponse
import com.example.playlistmaker.Search.data.dto.TrackSearchRequest
import com.example.playlistmaker.Search.domain.api.Resource
import com.example.playlistmaker.Search.domain.repositories.TrackRepository
import com.example.playlistmaker.Search.domain.models.Track
import com.example.playlistmaker.Search.data.network.NetworkClient

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {
    override fun searchTrack(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return if (response is TrackResponse) {
            val trackList = response.results.map { trackDto ->
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
                    previewUrl = trackDto.previewUrl
                )
            }
            Resource.Success(trackList)
        } else {
            Resource.Error(response.resultCode)
        }
    }
}
