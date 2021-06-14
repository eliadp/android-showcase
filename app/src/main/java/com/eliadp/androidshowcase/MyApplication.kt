package com.eliadp.androidshowcase

import android.app.Application
import com.eliadp.androidshowcase.data.dataModule
import com.eliadp.androidshowcase.domain.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(appModule, dataModule, domainModule)
        }
    }
}
