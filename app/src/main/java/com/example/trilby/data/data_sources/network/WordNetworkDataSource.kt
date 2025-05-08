package com.example.trilby.data.data_sources.network

import com.example.trilby.data.data_sources.network.model.NetworkWord

interface WordNetworkDataSource {
    suspend fun getWordList(query: String): List<NetworkWord>
}