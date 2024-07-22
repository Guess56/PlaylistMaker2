package com.example.playlistmaker.domain.api

interface Consumer<T> {
    fun consume(data: ConsumerData<T>)
}