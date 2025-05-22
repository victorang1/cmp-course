package org.example.cmpcourse.di

import org.example.cmpcourse.repository.AppRepository
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
}