package com.eliadp.androidshowcase.domain.task.usecases.internal

import com.eliadp.androidshowcase.domain.task.repositories.TaskRepository
import com.eliadp.androidshowcase.domain.task.usecases.DeleteCompletedTasksUseCase

class DeleteCompletedTasksUseCaseImpl(
    private val taskRepository: TaskRepository,
) : DeleteCompletedTasksUseCase {
    override suspend fun invoke() = taskRepository.deleteCompletedTasks()
}
