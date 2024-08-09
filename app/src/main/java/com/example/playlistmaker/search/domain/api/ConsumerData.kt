package com.example.playlistmaker.search.domain.api

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val message: Int) : ConsumerData<T>
}