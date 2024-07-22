package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTrack(expression : String):Resource<List<Track>>
}