package com.eliadp.androidshowcase.data.task

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eliadp.androidshowcase.data.task.daos.TaskDao
import com.eliadp.androidshowcase.data.task.entities.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        const val DATABASE_NAME = "todo_database"
    }
}
