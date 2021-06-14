package com.eliadp.androidshowcase.domain.task.entities

data class Task(
    val id: String,
    val name: String,
    val isImportant: Boolean,
    val isCompleted: Boolean,
    val createdAt: Long,
)
