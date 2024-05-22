package com.example.playlistmaker


    data class TrackResponse(val searchType: String,
                             val expression: String,
                             val results: List<Track>)
