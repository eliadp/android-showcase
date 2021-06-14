package com.eliadp.androidshowcase.data.task.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.repositories.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
) : PreferencesRepository {

    override fun load(): Flow<com.eliadp.androidshowcase.domain.task.entities.Preferences> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val sortOrder = SortOrder.valueOf(
                    preferences[SORT_ORDER] ?: SortOrder.BY_DATE.name
                )
                val hideCompleted = preferences[HIDE_COMPLETED] ?: false
                com.eliadp.androidshowcase.domain.task.entities.Preferences(
                    sortOrder,
                    hideCompleted,
                )
            }

    override suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[SORT_ORDER] = sortOrder.name
        }
    }

    override suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[HIDE_COMPLETED] = hideCompleted
        }
    }

    companion object PreferencesKeys {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val HIDE_COMPLETED = booleanPreferencesKey("hide_completed")
    }
}
