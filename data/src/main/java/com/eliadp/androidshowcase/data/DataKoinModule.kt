package com.eliadp.androidshowcase.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.eliadp.androidshowcase.data.task.TaskDatabase
import com.eliadp.androidshowcase.data.task.repositories.PreferencesRepositoryImpl
import com.eliadp.androidshowcase.data.task.repositories.TaskRepositoryImpl
import com.eliadp.androidshowcase.domain.task.repositories.PreferencesRepository
import com.eliadp.androidshowcase.domain.task.repositories.TaskRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {

    single {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            TaskDatabase::class.java,
            TaskDatabase.DATABASE_NAME,
        ).fallbackToDestructiveMigration().build()
    }

    single {
        preferencesDataStore(name = "todo_datastore").getValue(
            androidApplication().applicationContext,
            DataStore<Preferences>::javaClass,
        )
    }

    single { get<TaskDatabase>().taskDao() }

    single<TaskRepository> { TaskRepositoryImpl(get()) }
    single<PreferencesRepository> { PreferencesRepositoryImpl(get()) }
}
