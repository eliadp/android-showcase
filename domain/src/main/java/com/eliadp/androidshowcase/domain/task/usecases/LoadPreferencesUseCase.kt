package com.eliadp.androidshowcase.domain.task.usecases

import com.eliadp.androidshowcase.domain.task.entities.Preferences
import kotlinx.coroutines.flow.Flow

interface LoadPreferencesUseCase {
    operator fun invoke(): Flow<Preferences>
}
