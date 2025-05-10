package org.mipt.timetable.util

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import org.mipt.timetable.Database

actual fun createDriver(dbDir: String): SqlDriver {
    println(dbDir)
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:$dbDir")
    Database.Schema.create(driver)
    return driver
}
