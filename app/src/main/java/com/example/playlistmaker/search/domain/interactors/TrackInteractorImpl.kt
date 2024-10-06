package com.example.playlistmaker.search.domain.interactors

import com.example.playlistmaker.search.domain.api.Consumer
import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.api.TrackInteractor
import java.util.concurrent.Executors

import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map


class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {

    override fun searchTrack(expression: String): Flow<Pair<List<Track>?,Int?>> {
        return repository.searchTrack(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                Pair(result.data,null)
                }
                is Resource.Error -> {
                Pair(null,result.message)
                }
            }
        }
    }
}