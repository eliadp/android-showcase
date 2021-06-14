package com.eliadp.androidshowcase.domain.task.usecases.internal

import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.repositories.PreferencesRepository
import com.eliadp.androidshowcase.domain.task.usecases.UpdateSortOrderUseCase

class UpdateSortOrderUseCaseImpl(
    private val preferencesRepository: PreferencesRepository,
) : UpdateSortOrderUseCase {
    override suspend fun invoke(sortOrder: SortOrder) =
        preferencesRepository.updateSortOrder(sortOrder)
}
