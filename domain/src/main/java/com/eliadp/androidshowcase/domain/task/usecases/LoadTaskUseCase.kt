package com.eliadp.androidshowcase.domain.task.usecases

import com.eliadp.androidshowcase.domain.task.entities.Task

interface LoadTaskUseCase {
    suspend operator fun invoke(taskId: String): Task?
}
