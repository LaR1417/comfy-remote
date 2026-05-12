package com.comfyui.remote.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        private val KEY_SERVER_URL = stringPreferencesKey("server_url")
        private val KEY_API_KEY = stringPreferencesKey("api_key")
        private val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
    }

    val serverUrl: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_SERVER_URL] ?: ""
    }

    val apiKey: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_API_KEY] ?: ""
    }

    val themeMode: Flow<String> = dataStore.data.map { preferences ->
        preferences[KEY_THEME_MODE] ?: "system"
    }

    suspend fun saveServerUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[KEY_SERVER_URL] = url
        }
    }

    suspend fun saveApiKey(key: String) {
        dataStore.edit { preferences ->
            preferences[KEY_API_KEY] = key
        }
    }

    suspend fun saveThemeMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[KEY_THEME_MODE] = mode
        }
    }

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
