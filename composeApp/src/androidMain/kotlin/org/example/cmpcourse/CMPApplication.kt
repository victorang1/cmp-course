package org.example.cmpcourse

import android.app.Application
import org.example.cmpcourse.di.appModule
import org.koin.core.context.startKoin

class CMPApplication : Application() {

    companion object {
        lateinit var cmpApplication: Application
    }

    override fun onCreate() {
        super.onCreate()

        cmpApplication = this

        startKoin {
            modules(appModule)
        }
    }
}