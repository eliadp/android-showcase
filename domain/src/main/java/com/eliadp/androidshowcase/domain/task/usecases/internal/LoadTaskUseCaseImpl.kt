package com.eliadp.androidshowcase.domain.task.usecases.internal

import com.eliadp.androidshowcase.domain.task.entities.Task
import com.eliadp.androidshowcase.domain.task.repositories.TaskRepository
import com.eliadp.androidshowcase.domain.task.usecases.LoadTaskUseCase

class LoadTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : LoadTaskUseCase {
    override suspend fun invoke(taskId: String): Task? = taskRepository.getTask(taskId)
}
