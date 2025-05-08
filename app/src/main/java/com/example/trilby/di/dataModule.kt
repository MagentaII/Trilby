package com.example.trilby.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import androidx.room.Room
import com.example.trilby.data.repositories.auth_repository.AuthRepository
import com.example.trilby.data.repositories.auth_repository.DefaultAuthRepository
import com.example.trilby.data.repositories.word_repository.WordRepository
import com.example.trilby.data.repositories.word_repository.DefaultWordRepository
import com.example.trilby.data.data_sources.database.AppDatabase
import com.example.trilby.data.data_sources.database.dao.WordDao
import com.example.trilby.data.data_sources.firebase.UserFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.WordFirebaseDataSource
import com.example.trilby.data.data_sources.firebase.auth.FirebaseUserAuth
import com.example.trilby.data.data_sources.firebase.firestore.FirebaseWordFirestore

import com.example.trilby.data.data_sources.network.WordNetworkDataSource
import com.example.trilby.data.data_sources.network.retrofit.RetrofitWordNetwork
import com.example.trilby.data.data_sources.network.retrofit.RetrofitWordNetworkApi
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

// 1. Repository 模組
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindWordRepository(
        wordRepositoryImpl: DefaultWordRepository
    ): WordRepository

    @Singleton
    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: DefaultAuthRepository
    ): AuthRepository
}

// 2. Network 模組
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWordNetworkApi(retrofit: Retrofit): RetrofitWordNetworkApi {
        return retrofit.create(RetrofitWordNetworkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWordNetworkDataSource(
        networkApi: RetrofitWordNetworkApi
    ): WordNetworkDataSource {
        return RetrofitWordNetwork(networkApi)
    }
}

// 3. Local Storage 模組
@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    @Singleton
    fun provideWordDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "word_database"
        ).build()
    }

    @Provides
    fun provideWordDao(database: AppDatabase): WordDao {
        return database.wordDao()
    }
}

// 4. Firebase 模組
@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideFirebase(): Firebase = Firebase // 如果你有自定封裝

    @Provides
    @Singleton
    fun provideWordFirestoreDataSource(
        firebase: Firebase
    ): WordFirebaseDataSource {
        return FirebaseWordFirestore(firebase)
    }
}

// 5. Service 模組
@Module
@InstallIn(SingletonComponent::class)
abstract class ServiceModule {

    @Singleton
    @Binds
    abstract fun bindAuthService(
        authServiceImpl: FirebaseUserAuth
    ): UserFirebaseDataSource
}

// 6. 工具模組（例如 ExoPlayer）
@Module
@InstallIn(SingletonComponent::class)
object PlayerModule {

    @Provides
    @Singleton
    fun provideExoPlayer(@ApplicationContext context: Context): ExoPlayer {
        return ExoPlayer.Builder(context).build()
    }
}