package com.eliadp.androidshowcase.domain.task.usecases

interface DeleteCompletedTasksUseCase {
    suspend operator fun invoke()
}
