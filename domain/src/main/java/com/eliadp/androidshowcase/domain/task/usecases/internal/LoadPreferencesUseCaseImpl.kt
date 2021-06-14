package com.eliadp.androidshowcase.domain.task.usecases.internal

import com.eliadp.androidshowcase.domain.task.entities.Preferences
import com.eliadp.androidshowcase.domain.task.repositories.PreferencesRepository
import com.eliadp.androidshowcase.domain.task.usecases.LoadPreferencesUseCase
import kotlinx.coroutines.flow.Flow

class LoadPreferencesUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : LoadPreferencesUseCase {
    override fun invoke(): Flow<Preferences> = preferencesRepository.load()
}
