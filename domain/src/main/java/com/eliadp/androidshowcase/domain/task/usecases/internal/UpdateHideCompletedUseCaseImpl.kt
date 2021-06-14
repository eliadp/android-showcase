package com.eliadp.androidshowcase.domain.task.usecases.internal

import com.eliadp.androidshowcase.domain.task.repositories.PreferencesRepository
import com.eliadp.androidshowcase.domain.task.usecases.UpdateHideCompletedUseCase

class UpdateHideCompletedUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : UpdateHideCompletedUseCase {
    override suspend fun invoke(hideCompleted: Boolean) =
        preferencesRepository.updateHideCompleted(hideCompleted)
}
