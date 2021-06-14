package com.eliadp.androidshowcase.domain.task.usecases.internal

import com.eliadp.androidshowcase.domain.task.repositories.TaskRepository
import com.eliadp.androidshowcase.domain.task.usecases.UpdateTaskUseCase

class UpdateTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : UpdateTaskUseCase {
    override suspend fun invoke(
        taskId: String,
        name: String,
        isImportant: Boolean,
        isCompleted: Boolean,
    ) {
        val task = taskRepository.getTask(taskId)
        task?.let {
            taskRepository.update(
                task.copy(
                    name = name,
                    isImportant = isImportant,
                    isCompleted = isCompleted,
                )
            )
        }
    }
}
