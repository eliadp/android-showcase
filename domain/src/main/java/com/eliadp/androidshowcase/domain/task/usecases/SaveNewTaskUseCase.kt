package com.eliadp.androidshowcase.domain.task.usecases

interface SaveNewTaskUseCase {
    suspend operator fun invoke(
        name: String,
        isImportant: Boolean,
    )
}
