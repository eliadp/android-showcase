package com.eliadp.androidshowcase.domain.task.entities

enum class SortOrder { BY_NAME, BY_DATE }

data class Preferences(
    val sortOrder: SortOrder,
    val hideCompleted: Boolean,
)
