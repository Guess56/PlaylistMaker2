package com.example.playlistmaker.search.domain.api

sealed interface Resource<T> {
    data class Success<T>(val data: T): Resource<T>
    data class Error<T>(val message: Int): Resource<T>
}