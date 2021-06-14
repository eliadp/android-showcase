package com.eliadp.androidshowcase.domain

import com.eliadp.androidshowcase.domain.task.usecases.*
import com.eliadp.androidshowcase.domain.task.usecases.internal.*
import org.koin.dsl.module

val domainModule = module {

    single<LoadTaskListUseCase> { LoadTaskListUseCaseImpl(get()) }
    single<LoadTaskUseCase> { LoadTaskUseCaseImpl(get()) }
    single<SaveNewTaskUseCase> { SaveNewTaskUseCaseImpl(get()) }
    single<UpdateTaskUseCase> { UpdateTaskUseCaseImpl(get()) }
    single<DeleteCompletedTasksUseCase> { DeleteCompletedTasksUseCaseImpl(get()) }

    single<LoadPreferencesUseCase> { LoadPreferencesUseCaseImpl(get()) }
    single<UpdateSortOrderUseCase> { UpdateSortOrderUseCaseImpl(get()) }
    single<UpdateHideCompletedUseCase> { UpdateHideCompletedUseCaseImpl(get()) }
}
