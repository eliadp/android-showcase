package com.eliadp.androidshowcase.domain.task.usecases

interface UpdateHideCompletedUseCase {
    suspend operator fun invoke(
        hideCompleted: Boolean,
    )
}
