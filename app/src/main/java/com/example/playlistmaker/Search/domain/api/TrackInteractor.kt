package com.example.playlistmaker.Search.domain.api


import com.example.playlistmaker.Search.domain.models.Track

interface TrackInteractor {
     fun searchTrack(expression: String, consumer: Consumer<List<Track>>)
}
