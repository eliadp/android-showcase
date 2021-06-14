package com.eliadp.androidshowcase.data.task.repositories

import com.eliadp.androidshowcase.data.task.daos.TaskDao
import com.eliadp.androidshowcase.data.task.entities.toDataEntity
import com.eliadp.androidshowcase.data.task.entities.toDomainEntity
import com.eliadp.androidshowcase.domain.task.entities.SortOrder
import com.eliadp.androidshowcase.domain.task.entities.Task
import com.eliadp.androidshowcase.domain.task.repositories.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun getTask(id: String?): Task? {
        return id?.let {
            taskDao.getTask(id)?.toDomainEntity()
        } ?: run { null }
    }

    override fun getTasks(
        query: String,
        sortOrder: SortOrder,
        hideCompleted: Boolean,
    ): Flow<List<Task>> =
        when (sortOrder) {
            SortOrder.BY_DATE -> taskDao.getTasksSortedByDateCreated(query, hideCompleted)
            SortOrder.BY_NAME -> taskDao.getTasksSortedByName(query, hideCompleted)
        }.map { taskList -> taskList.map { it.toDomainEntity() } }

    override suspend fun update(task: Task) {
        taskDao.update(task.toDataEntity())
    }

    override suspend fun insert(task: Task) {
        taskDao.insert(task.toDataEntity())
    }

    override suspend fun deleteCompletedTasks() {
        taskDao.deleteCompletedTasks()
    }
}
