package com.eliadp.androidshowcase.domain.task.repositories

import com.eliadp.androidshowcase.domain.task.entities.Preferences
import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun load(): Flow<Preferences>

    suspend fun updateSortOrder(sortOrder: SortOrder)

    suspend fun updateHideCompleted(hideCompleted: Boolean)
}
