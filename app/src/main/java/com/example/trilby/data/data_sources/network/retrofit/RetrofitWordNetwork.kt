package com.example.trilby.data.data_sources.network.retrofit

import com.example.trilby.BuildConfig
import com.example.trilby.data.data_sources.network.WordNetworkDataSource
import com.example.trilby.data.data_sources.network.model.NetworkWord
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

//private const val YOUR_API_KEY = "1d6b5749-ab1a-46e8-be28-3cf6b3791d66"

interface RetrofitWordNetworkApi {
    @GET("api/v3/references/collegiate/json/{word}")
    suspend fun getWordList(
        @Path("word") word: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): List<NetworkWord>
}

internal class RetrofitWordNetwork @Inject constructor(
    private val networkApi: RetrofitWordNetworkApi
) : WordNetworkDataSource {
    override suspend fun getWordList(query: String): List<NetworkWord> {
        return networkApi.getWordList(word = query)
    }
}