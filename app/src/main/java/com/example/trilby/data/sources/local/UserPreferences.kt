package com.example.trilby.data.sources.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.trilby.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class UserPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    private object PreferencesKeys {
        val USER_UID = stringPreferencesKey("user_uid")
    }

    suspend fun getUserUid(): String? {
        val preferences = dataStore.data.first()
        return preferences[PreferencesKeys.USER_UID]
    }

    // 儲存 UserUid
    suspend fun saveUserUid(uid: String?) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_UID] = uid ?: "null"
        }
    }

    // 清除 UserUid
    suspend fun clearUserUid() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_UID)
        }
    }
}