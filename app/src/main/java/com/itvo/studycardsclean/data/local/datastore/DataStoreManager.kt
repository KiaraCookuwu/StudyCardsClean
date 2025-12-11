package com.itvo.studycardsclean.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Extensión para crear el DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "study_settings")

class DataStoreManager @Inject constructor(private val context: Context) {

    companion object {
        val IS_GRID_VIEW = booleanPreferencesKey("is_grid_view")
    }

    // Guardar preferencia
    suspend fun saveViewMode(isGrid: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_GRID_VIEW] = isGrid
        }
    }

    // Leer preferencia (Flow)
    val isGridViewFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_GRID_VIEW] ?: false // Por defecto es lista (false)
        }
}