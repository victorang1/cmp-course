package org.example.cmpcourse.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.example.cmpcourse.TodoDatabase
import java.util.Properties

actual class DriverFactory {

    actual fun createDriver(): SqlDriver? {
        return JdbcSqliteDriver("jdbc:sqlite:test.db", Properties(), TodoDatabase.Schema)
    }
}