package com.example.playlistmaker.Search.data.network

import com.example.playlistmaker.Search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto:Any): Response
}