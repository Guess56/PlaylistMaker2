package com.example.playlistmaker.search.domain.api


import com.example.playlistmaker.search.domain.models.Track

interface TrackInteractor {
     fun searchTrack(expression: String, consumer: Consumer<List<Track>>)
}
