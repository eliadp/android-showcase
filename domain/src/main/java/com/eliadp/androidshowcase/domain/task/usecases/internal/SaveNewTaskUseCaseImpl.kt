package com.eliadp.androidshowcase.domain.task.usecases.internal

import com.eliadp.androidshowcase.domain.task.entities.Task
import com.eliadp.androidshowcase.domain.task.repositories.TaskRepository
import com.eliadp.androidshowcase.domain.task.usecases.SaveNewTaskUseCase
import java.util.*

class SaveNewTaskUseCaseImpl(
    private val taskRepository: TaskRepository,
) : SaveNewTaskUseCase {
    override suspend fun invoke(name: String, isImportant: Boolean) {
        val newTask = Task(
            id = UUID.randomUUID().toString(),
            name = name,
            isImportant = isImportant,
            isCompleted = false,
            createdAt = System.currentTimeMillis(),
        )
        taskRepository.insert(newTask)
    }
}
