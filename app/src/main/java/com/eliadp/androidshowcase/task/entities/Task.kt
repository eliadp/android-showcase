package com.eliadp.androidshowcase.task.entities

import com.eliadp.androidshowcase.domain.task.entities.Task
import java.text.DateFormat

data class TaskUIModel(
    val id: String,
    val name: String,
    val creationDate: String,
    val isCompleted: Boolean,
    val isImportant: Boolean,
)

fun Task.toUIModel() = TaskUIModel(
    id = id,
    name = name,
    isImportant = isImportant,
    isCompleted = isCompleted,
    creationDate = DateFormat.getDateTimeInstance().format(createdAt),
)
