package com.eliadp.androidshowcase.domain.task.usecases

import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.entities.Task
import kotlinx.coroutines.flow.Flow

interface LoadTaskListUseCase {
    operator fun invoke(
        query: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean,
    ): Flow<List<Task>>
}
