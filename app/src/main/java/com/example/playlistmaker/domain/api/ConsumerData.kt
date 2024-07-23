package com.example.playlistmaker.domain.api

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val message: Int) : ConsumerData<T>
}