package org.example.cmpcourse.database

import app.cash.sqldelight.db.SqlDriver

expect class DriverFactory() {
    fun createDriver(): SqlDriver?
}