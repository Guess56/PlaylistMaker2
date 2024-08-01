package com.example.playlistmaker.domain.repositories

import com.example.playlistmaker.domain.api.Resource
import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression : String): Resource<List<Track>>
}