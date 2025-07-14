package org.example.cmpcourse.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.example.cmpcourse.TodoDatabase

actual class DriverFactory {

    actual fun createDriver(): SqlDriver? {
        return NativeSqliteDriver(
            schema = TodoDatabase.Schema,
            name = "todo.db"
        )
    }
}