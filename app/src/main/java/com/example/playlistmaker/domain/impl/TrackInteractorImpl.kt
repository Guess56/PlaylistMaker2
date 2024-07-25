package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.Consumer
import com.example.playlistmaker.domain.api.ConsumerData
import com.example.playlistmaker.domain.api.Resource
import com.example.playlistmaker.domain.interactors.TrackInteractor
import java.util.concurrent.Executors

import com.example.playlistmaker.domain.repositories.TrackRepository
import com.example.playlistmaker.domain.models.Track


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