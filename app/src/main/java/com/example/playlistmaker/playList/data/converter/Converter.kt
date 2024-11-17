package com.example.playlistmaker.playList.data.converter

import com.example.playlistmaker.playList.data.db.entity.PlayListTrackEntity
import com.example.playlistmaker.search.domain.models.Track

class Converter {
    fun map(track: PlayListTrackEntity): Track {
        return Track(track.trackId.toString(),track.trackName, track.country,
            track.releaseDate, track.collectionName,track.primaryGenreName, track.artistName, track.trackTimeMillis,
            track.artworkUrl100, track.previewUrl, track.inFavorite,System.currentTimeMillis())
    }

    fun map(track: Track): PlayListTrackEntity {
        return PlayListTrackEntity(track.trackId.toLong(),track.trackName,track.country,track.releaseDate,
            track.collectionName,track.primaryGenreName,track.artistName,
            track.trackTimeMillis,track.artworkUrl100,track.previewUrl,track.inFavorite, System.currentTimeMillis())
    }
}