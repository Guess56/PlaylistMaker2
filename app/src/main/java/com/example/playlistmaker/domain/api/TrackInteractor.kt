package com.example.playlistmaker.domain.api


import com.example.playlistmaker.domain.api.Consumer
import com.example.playlistmaker.domain.models.Track

interface TrackInteractor {
     fun searchTrack(expression: String, consumer: Consumer<List<Track>>)
}
