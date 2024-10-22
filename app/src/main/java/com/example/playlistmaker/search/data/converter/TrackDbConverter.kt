package com.example.playlistmaker.search.data.converters

import com.example.playlistmaker.search.data.db.entity.TrackEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConverter {
    fun map(track: TrackDto):TrackEntity {
        return TrackEntity(track.trackId,track.trackName,track.country,track.releaseDate,
            track.collectionName,track.primaryGenreName,track.artistName,
            track.trackTimeMillis,track.artworkUrl100,track.previewUrl, track.inFavorite)
    }
    fun map(track: TrackEntity): Track {
        return Track(track.trackId,track.trackName, track.country,
            track.releaseDate, track.collectionName,track.primaryGenreName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.previewUrl, track.inFavorite)
    }

    fun map(track:Track):TrackEntity{
        return TrackEntity(track.trackId,track.trackName,track.country,track.releaseDate,
             track.collectionName,track.primaryGenreName,track.artistName,
             track.trackTimeMillis,track.artworkUrl100,track.previewUrl, track.inFavorite)
    }
}
