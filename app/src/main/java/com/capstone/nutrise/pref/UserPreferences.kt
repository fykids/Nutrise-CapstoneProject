package com.capstone.nutrise.pref

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// untuk mode malam
val Context.dataStore by preferencesDataStore(name = "user_preferences")

class UserPreferences(private val context : Context) {

    companion object {
        private val NIGHT_MODE_KEY = booleanPreferencesKey("night_mode")
    }

    val isNightMode: Flow<Boolean> = context.dataStore.data
        .map { preference ->
            preference[NIGHT_MODE_KEY] == true
        }

    suspend fun settingNightMode(isNightMode : Boolean) {
        context.dataStore.edit { preference ->
            preference[NIGHT_MODE_KEY] = isNightMode
        }
    }
}