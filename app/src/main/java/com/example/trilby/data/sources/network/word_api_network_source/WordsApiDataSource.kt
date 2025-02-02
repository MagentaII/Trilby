package com.example.trilby.data.sources.network.word_api_network_source

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val YOUR_API_KEY = "1d6b5749-ab1a-46e8-be28-3cf6b3791d66"


interface DictionaryApiService {

    @GET("api/v3/references/collegiate/json/{word}")
    suspend fun searchWords(
        @Path("word") word: String,
        @Query("key") apiKey: String = YOUR_API_KEY
    ): List<NetworkWord>
}