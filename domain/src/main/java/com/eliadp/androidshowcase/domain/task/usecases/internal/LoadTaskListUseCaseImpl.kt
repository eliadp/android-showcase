package com.eliadp.androidshowcase.domain.task.usecases.internal

import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.repositories.TaskRepository
import com.eliadp.androidshowcase.domain.task.usecases.LoadTaskListUseCase

class LoadTaskListUseCaseImpl(
    private val taskRepository: TaskRepository,
) : LoadTaskListUseCase {
    override fun invoke(query: String, sortOrder: SortOrder, hideCompleted: Boolean) =
        taskRepository.getTasks(
            query,
            sortOrder,
            hideCompleted,
        )
}
