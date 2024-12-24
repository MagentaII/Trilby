package com.example.trilby.data.sources.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val YOUR_API_KEY = "faab7d2f-864a-4f5d-94ca-dfde3ffd3b88"


interface DictionaryApiService {

    @GET("api/v3/references/learners/json/{word}")
    suspend fun searchWords(
        @Path("word") word: String,
        @Query("key") apiKey: String = YOUR_API_KEY,
    ): List<NetworkWords>
}