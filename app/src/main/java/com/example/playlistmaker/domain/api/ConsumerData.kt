package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

sealed interface ConsumerData<T> {
    data class Data<T>(val value: T) : ConsumerData<T>
    data class Error<T>(val message: Int) : ConsumerData<T>
}