package com.eliadp.androidshowcase

import com.eliadp.androidshowcase.task.TaskListViewModel
import com.eliadp.androidshowcase.task.addedit.AddEditTaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        TaskListViewModel(
            loadTaskListUseCase = get(),
            updateTaskUseCase = get(),
            deleteCompletedTasksUseCase = get(),
            loadPreferencesUseCase = get(),
            updateSortOrderUseCase = get(),
            updateHideCompletedUseCase = get(),
        )
    }
    viewModel { (taskId: String?) ->
        AddEditTaskViewModel(
            taskId = taskId,
            loadTaskUseCase = get(),
            newTaskUseCase = get(),
            updateTaskUseCase = get(),
        )
    }
}
