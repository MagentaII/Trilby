package com.example.trilby

import com.example.trilby.util.timber.MultiTagTree
import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@HiltAndroidApp
class TrilbyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.LOGGING_ENABLED) {
            Timber.plant(MultiTagTree())
        }
    }
}