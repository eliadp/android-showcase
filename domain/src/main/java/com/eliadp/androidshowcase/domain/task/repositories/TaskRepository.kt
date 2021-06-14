package com.eliadp.androidshowcase.domain.task.repositories

import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.entities.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getTask(id: String?): Task?

    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Task>>

    suspend fun update(task: Task)

    suspend fun insert(task: Task)

    suspend fun deleteCompletedTasks()
}
