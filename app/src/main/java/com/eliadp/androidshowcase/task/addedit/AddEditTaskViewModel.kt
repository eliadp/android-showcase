package com.eliadp.androidshowcase.task.addedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eliadp.androidshowcase.domain.task.entities.Task
import com.eliadp.androidshowcase.domain.task.usecases.LoadTaskUseCase
import com.eliadp.androidshowcase.domain.task.usecases.SaveNewTaskUseCase
import com.eliadp.androidshowcase.domain.task.usecases.UpdateTaskUseCase
import com.eliadp.androidshowcase.task.entities.TaskUIModel
import com.eliadp.androidshowcase.task.entities.toUIModel
import kotlinx.coroutines.launch

sealed class AddEditState {
    object Loading : AddEditState()

    data class Data(
        val task: TaskUIModel,
    ) : AddEditState()
}

sealed class AddEditAction {
    object ShowInvalidInputMessage : AddEditAction()
    data class NavigateBackWithResult(val result: Int) : AddEditAction()
}

class AddEditTaskViewModel(
    private val taskId: String?,
    private val loadTaskUseCase: LoadTaskUseCase,
    private val newTaskUseCase: SaveNewTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
) : ViewModel() {

    private var actionHandler: ((action: AddEditAction) -> Unit)? = null

    private val _state: MutableLiveData<AddEditState> = MutableLiveData(AddEditState.Loading)
    val state: LiveData<AddEditState> = _state

    init {
        viewModelScope.launch {
            val task = loadTask()
            if (task != null) {
                _state.value = AddEditState.Data(task.toUIModel())
            }
        }
    }

    fun handleActions(actionHandler: (action: AddEditAction) -> Unit) {
        this.actionHandler = actionHandler
    }


    fun onSaveClicked(name: String, isImportant: Boolean) {
        if (name.isBlank()) {
            actionHandler?.invoke(AddEditAction.ShowInvalidInputMessage)
            return
        }

        viewModelScope.launch {
            val task = loadTask()
            if (task != null) {
                with(task) {
                    updateTaskUseCase(
                        taskId = id,
                        name = name,
                        isImportant = isImportant,
                        isCompleted = isCompleted,
                    )
                }
                actionHandler?.invoke(
                    AddEditAction.NavigateBackWithResult(EDIT_TASK_RESULT_OK)
                )
            } else {
                newTaskUseCase(
                    name = name,
                    isImportant = isImportant,
                )
                actionHandler?.invoke(
                    AddEditAction.NavigateBackWithResult(ADD_TASK_RESULT_OK)
                )
            }
        }
    }

    private suspend fun loadTask(): Task? {
        return if (taskId != null) {
            loadTaskUseCase(taskId)
        } else {
            null
        }
    }

    companion object {
        const val ADD_TASK_RESULT_OK = 127
        const val EDIT_TASK_RESULT_OK = 128
    }
}
