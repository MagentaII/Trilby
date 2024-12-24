package com.example.trilby.data.di

import com.example.trilby.data.repositories.WordRepository
import com.example.trilby.data.repositories.WordRepositoryImpl
import com.example.trilby.data.sources.network.NetworkDataSource
import com.example.trilby.data.sources.network.WordsNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindWordRepository(
        wordRepositoryImpl: WordRepositoryImpl
    ): WordRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkDataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(
        wordsNetworkDataSource: WordsNetworkDataSource
    ): NetworkDataSource
}