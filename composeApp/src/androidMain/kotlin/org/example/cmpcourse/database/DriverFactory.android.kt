package org.example.cmpcourse.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import org.example.cmpcourse.CMPApplication
import org.example.cmpcourse.TodoDatabase

actual class DriverFactory {

    actual fun createDriver(): SqlDriver? {
        return AndroidSqliteDriver(
            schema = TodoDatabase.Schema,
            context = CMPApplication.cmpApplication.applicationContext,
            name = "todo.db"
        )
    }
}