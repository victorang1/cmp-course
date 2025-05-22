package org.example.cmpcourse

import android.app.Application
import org.example.cmpcourse.di.appModule
import org.koin.core.context.startKoin

class CMPApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(appModule)
        }
    }
}