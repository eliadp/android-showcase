package com.eliadp.androidshowcase.domain.task.usecases

import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.entities.Task

interface UpdateSortOrderUseCase {
    suspend operator fun invoke(
        sortOrder: SortOrder,
    )
}
