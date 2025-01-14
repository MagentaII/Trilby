package com.example.trilby.data.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.data.repositories.auth_repository.AuthRepositoryImpl
import com.example.trilby.data.repositories.word_repository.WordRepository
import com.example.trilby.data.repositories.word_repository.WordRepositoryImpl
import com.example.trilby.data.sources.local.AppDatabase
import com.example.trilby.data.sources.local.WordDao
import com.example.trilby.data.sources.network.auth_network_source.AuthService
import com.example.trilby.data.sources.network.auth_network_source.AuthServiceImpl
import com.example.trilby.data.sources.network.word_network_source.DictionaryApiService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
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

    @Singleton
    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
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
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
            .wordDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun providerFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Singleton
    @Binds
    abstract fun bindAuthService(
        authServiceImpl: AuthServiceImpl
    ): AuthService
}

@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }
}