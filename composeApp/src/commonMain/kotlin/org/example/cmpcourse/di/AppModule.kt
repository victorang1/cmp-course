package org.example.cmpcourse.di

import com.russhwolf.settings.Settings
import org.example.cmpcourse.TodoDatabase
import org.example.cmpcourse.database.DriverFactory
import org.example.cmpcourse.repository.AppRepository
import org.example.cmpcourse.repository.TodoRepository
import org.example.cmpcourse.repository.TodoRepositoryImpl
import org.example.cmpcourse.repository.WebTodoRepositoryImpl
import org.koin.dsl.bind
import org.koin.dsl.module

/*

    Singleton
    Factory

    class Car {

    }

    // Factory
    val myCar = Car()
    val motherCar = Car()

    // Singleton
    val familyCar = Car()
 */

val appModule = module {
    single<AppRepository> {
        AppRepository()
    }
    single {
        val sqlDriver = DriverFactory().createDriver()
        if (sqlDriver == null) {
            WebTodoRepositoryImpl(Settings())
        } else {
            val database = TodoDatabase.invoke(sqlDriver)
            TodoRepositoryImpl(database)
        }
    }.bind<TodoRepository>()
}