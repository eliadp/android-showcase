package com.eliadp.androidshowcase.data.task.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.eliadp.androidshowcase.domain.task.entities.Task

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val isImportant: Boolean,
    val isCompleted: Boolean,
    val createdAt: Long,
)

fun Task.toDataEntity() = TaskEntity(
    id = id,
    name = name,
    isImportant = isImportant,
    isCompleted = isCompleted,
    createdAt = createdAt,
)

fun TaskEntity.toDomainEntity() = Task(
    id = id,
    name = name,
    isImportant = isImportant,
    isCompleted = isCompleted,
    createdAt = createdAt,
)
