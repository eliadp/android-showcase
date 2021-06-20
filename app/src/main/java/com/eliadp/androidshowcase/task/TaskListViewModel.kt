package com.eliadp.androidshowcase.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.usecases.*
import com.eliadp.androidshowcase.task.addedit.AddEditTaskViewModel.Companion.ADD_TASK_RESULT_OK
import com.eliadp.androidshowcase.task.addedit.AddEditTaskViewModel.Companion.EDIT_TASK_RESULT_OK
import com.eliadp.androidshowcase.task.entities.TaskUIModel
import com.eliadp.androidshowcase.task.entities.toUIModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class TaskListState {
    object Loading : TaskListState()

    data class Empty(
        val query: String,
    ) : TaskListState()

    data class Data(
        val tasks: List<TaskUIModel>,
        val hideCompleted: Boolean,
        val query: String,
    ) : TaskListState()
}

sealed class TaskListAction {
    object NavigateToAddTask : TaskListAction()
    data class NavigateToEditTask(val taskId: String) : TaskListAction()
    object ShowTaskAddedConfirmationMessage : TaskListAction()
    object ShowTaskUpdatedConfirmationMessage : TaskListAction()
    object ShowDeleteConfirmationDialog : TaskListAction()
    data class SetupMenu(val hideCompleted: Boolean) : TaskListAction()
}

class TaskListViewModel(
    private val loadTaskListUseCase: LoadTaskListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteCompletedTasksUseCase: DeleteCompletedTasksUseCase,
    loadPreferencesUseCase: LoadPreferencesUseCase,
    private val updateSortOrderUseCase: UpdateSortOrderUseCase,
    private val updateHideCompletedUseCase: UpdateHideCompletedUseCase,
) : ViewModel() {

    private var actionHandler: ((action: TaskListAction) -> Unit)? = null

    private val searchQuery = MutableStateFlow("")
    private val preferences = loadPreferencesUseCase()
    private val tasks = combine(
        searchQuery,
        preferences,
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        loadTaskListUseCase(
            query,
            filterPreferences.sortOrder,
            filterPreferences.hideCompleted,
        )
    }

    val state: LiveData<TaskListState> = tasks
        .map { list ->
            if (list.isNotEmpty()) {
                TaskListState.Data(
                    tasks = list.map { it.toUIModel() },
                    hideCompleted = preferences.first().hideCompleted,
                    query = searchQuery.value,
                )
            } else {
                TaskListState.Empty(searchQuery.value)
            }
        }
        .asLiveData()

    fun handleActions(actionHandler: (action: TaskListAction) -> Unit) {
        this.actionHandler = actionHandler
    }


    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        updateSortOrderUseCase(sortOrder)
    }

    fun onHideCompletedClicked(hideCompleted: Boolean) = viewModelScope.launch {
        updateHideCompletedUseCase(hideCompleted)
    }

    fun onTaskSelected(task: TaskUIModel) {
        actionHandler?.invoke(TaskListAction.NavigateToEditTask(task.id))
    }

    fun onTaskCheckedChanged(task: TaskUIModel, isChecked: Boolean) = viewModelScope.launch {
        updateTaskUseCase(
            taskId = task.id,
            name = task.name,
            isCompleted = isChecked,
            isImportant = task.isImportant,
        )
    }

    fun onAddNewTaskClicked() {
        actionHandler?.invoke(TaskListAction.NavigateToAddTask)
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_TASK_RESULT_OK -> {
                actionHandler?.invoke(TaskListAction.ShowTaskAddedConfirmationMessage)
            }
            EDIT_TASK_RESULT_OK -> {
                actionHandler?.invoke(TaskListAction.ShowTaskUpdatedConfirmationMessage)
            }
        }
    }

    fun onDeleteAllCompletedClicked() {
        actionHandler?.invoke(TaskListAction.ShowDeleteConfirmationDialog)
    }

    fun onDeleteConfirmClicked() = viewModelScope.launch {
        deleteCompletedTasksUseCase()
    }

    fun onQueryTextChanged(query: String) {
        searchQuery.value = query
    }

    fun onMenuCreated() {
        val currentState = state.value as? TaskListState.Data ?: return
        actionHandler?.invoke(TaskListAction.SetupMenu(currentState.hideCompleted))
    }
}
