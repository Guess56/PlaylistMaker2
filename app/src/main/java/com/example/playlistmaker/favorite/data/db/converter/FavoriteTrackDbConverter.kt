package com.example.playlistmaker.favorite.data.db.converter

import com.example.playlistmaker.favorite.data.db.entity.FavoriteEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track

class FavoriteTrackDbConverter {
    fun map(track: TrackDto): FavoriteEntity {
        return FavoriteEntity(track.trackId.toLong(),track.trackName,track.country,track.releaseDate,
            track.collectionName,track.primaryGenreName,track.artistName,
            track.trackTimeMillis,track.artworkUrl100,track.previewUrl, track.inFavorite)
    }
    fun map(track: FavoriteEntity): Track {
        return Track(track.trackId.toString(),track.trackName, track.country,
            track.releaseDate, track.collectionName,track.primaryGenreName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.previewUrl, track.inFavorite)
    }
}