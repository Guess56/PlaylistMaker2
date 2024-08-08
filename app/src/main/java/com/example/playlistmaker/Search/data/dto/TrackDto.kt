package com.example.playlistmaker.Search.data.dto

class TrackDto (
    val trackId:String,
    val trackName:String,
    val country:String,
    val releaseDate:java.util.Date,
    val collectionName:String,
    val primaryGenreName:String,
    val artistName:String,
    val trackTimeMillis:Int,
    val artworkUrl100:String,
    val previewUrl : String
)