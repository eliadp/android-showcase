package com.eliadp.androidshowcase.domain.task.usecases

interface UpdateTaskUseCase {
    suspend operator fun invoke(
        taskId: String,
        name: String,
        isImportant: Boolean,
        isCompleted: Boolean,
    )
}
