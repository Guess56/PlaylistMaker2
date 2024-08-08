package com.example.playlistmaker.Search.domain.api

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}