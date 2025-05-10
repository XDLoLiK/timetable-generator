package org.mipt.timetable.data.model

import org.mipt.timetable.util.getApplicationSupportDirectory

data class Settings(
    val serverUrl: String = "http://0.0.0.0:8080",
    val dbDir: String = "${getApplicationSupportDirectory()}/timetable.db"
)
