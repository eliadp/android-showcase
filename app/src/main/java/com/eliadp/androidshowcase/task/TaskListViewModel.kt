package com.eliadp.androidshowcase.task

import androidx.lifecycle.*
import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.usecases.*
import com.eliadp.androidshowcase.task.addedit.AddEditTaskViewModel.Companion.ADD_TASK_RESULT_OK
import com.eliadp.androidshowcase.task.addedit.AddEditTaskViewModel.Companion.EDIT_TASK_RESULT_OK
import com.eliadp.androidshowcase.task.entities.TaskUIModel
import com.eliadp.androidshowcase.task.entities.toUIModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

sealed class TaskListState {
    object Loading : TaskListState()

    data class Empty(
        val hideCompleted: Boolean,
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
    data class SetupMenu(val hideCompleted: Boolean, val query: String) : TaskListAction()
}

class TaskListViewModel(
    savedState: SavedStateHandle,
    private val loadTaskListUseCase: LoadTaskListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteCompletedTasksUseCase: DeleteCompletedTasksUseCase,
    loadPreferencesUseCase: LoadPreferencesUseCase,
    private val updateSortOrderUseCase: UpdateSortOrderUseCase,
    private val updateHideCompletedUseCase: UpdateHideCompletedUseCase,
) : ViewModel() {

    private var actionHandler: ((action: TaskListAction) -> Unit)? = null

    private val searchQuery = savedState.getLiveData("searchQuery", SEARCH_QUERY_DEFAULT)
    private val preferences = loadPreferencesUseCase()
    private val tasks = combine(
        searchQuery.asFlow(),
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
                    query = searchQuery.value ?: SEARCH_QUERY_DEFAULT,
                )
            } else {
                TaskListState.Empty(
                    hideCompleted = preferences.first().hideCompleted,
                    query = searchQuery.value ?: SEARCH_QUERY_DEFAULT,
                )
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
        val hideCompleted = when (val currentState = state.value) {
            is TaskListState.Data -> currentState.hideCompleted
            is TaskListState.Empty -> currentState.hideCompleted
            else -> return
        }
        val query = when (val currentState = state.value) {
            is TaskListState.Data -> currentState.query
            is TaskListState.Empty -> currentState.query
            else -> return
        }
        actionHandler?.invoke(
            TaskListAction.SetupMenu(
                hideCompleted,
                query,
            )
        )
    }

    companion object {
        private const val SEARCH_QUERY_DEFAULT = ""
    }
}
