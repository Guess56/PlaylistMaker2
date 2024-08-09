package com.example.playlistmaker.search.domain.repositories

import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression : String): Resource<List<Track>>
}