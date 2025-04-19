package org.mipt.timetable

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
