package com.example.playlistmaker.search.domain.api

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}