package com.example.playlistmaker.search.domain.repositories

import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack(expression : String): Flow<Resource<List<Track>>>
}