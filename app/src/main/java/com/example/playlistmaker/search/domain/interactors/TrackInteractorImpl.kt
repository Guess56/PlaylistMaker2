package com.example.playlistmaker.search.domain.interactors

import com.example.playlistmaker.search.domain.api.Consumer
import com.example.playlistmaker.search.domain.api.ConsumerData
import com.example.playlistmaker.search.domain.api.Resource
import com.example.playlistmaker.search.domain.api.TrackInteractor
import java.util.concurrent.Executors

import com.example.playlistmaker.search.domain.repositories.TrackRepository
import com.example.playlistmaker.search.domain.models.Track


class TrackInteractorImpl(private val repository: TrackRepository): TrackInteractor {
    private val executor = Executors.newCachedThreadPool()
        override fun searchTrack(expression: String, consumer: Consumer<List<Track>>) {

        executor.execute {
            val response = repository.searchTrack(expression)

            when (response) {
                is Resource.Success -> {
                    consumer.consume(ConsumerData.Data(response.data))
                }

                is Resource.Error -> {
                    consumer.consume(ConsumerData.Error(response.message))
                }
            }
        }
    }
}