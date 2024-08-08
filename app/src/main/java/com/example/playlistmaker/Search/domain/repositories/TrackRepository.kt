package com.example.playlistmaker.Search.domain.repositories

import com.example.playlistmaker.Search.domain.api.Resource
import com.example.playlistmaker.Search.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression : String): Resource<List<Track>>
}