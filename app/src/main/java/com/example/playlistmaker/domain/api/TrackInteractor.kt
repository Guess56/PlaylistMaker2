package com.example.playlistmaker.domain.api


import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.domain.models.Track
import java.lang.Exception

interface TrackInteractor {
     fun searchTrack(expression: String, consumer:Consumer<List<Track>>) {

    }
}
