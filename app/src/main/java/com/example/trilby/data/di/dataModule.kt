package com.example.trilby.data.di

import android.content.Context
import androidx.room.Room
import com.example.trilby.data.repositories.WordRepository
import com.example.trilby.data.repositories.WordRepositoryImpl
import com.example.trilby.data.sources.local.AppDatabase
import com.example.trilby.data.sources.local.WordDao
import com.example.trilby.data.sources.network.DictionaryApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://www.dictionaryapi.com/"

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
object NetworkDataSourceModule {

    @Singleton
    @Provides
    fun providerNetworkDataSource(): DictionaryApiService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(DictionaryApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LocalDataSourceModule {

    @Singleton
    @Provides
    fun providerLocalDataSource(@ApplicationContext context: Context): WordDao {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "word_database"
        ).build().wordDao()
    }
}