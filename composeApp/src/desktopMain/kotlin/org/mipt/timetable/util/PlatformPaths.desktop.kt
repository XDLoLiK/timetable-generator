package org.mipt.timetable.util

import java.io.File

actual fun getApplicationSupportDirectory(): String {
    val userHome = System.getProperty("user.home")
    val appName = "org.mipt.timetable"

    // Platform-specific application support directory
    val directory = when {
        System.getProperty("os.name").lowercase().contains("mac") ->
            "$userHome/Library/Application Support/$appName"

        System.getProperty("os.name").lowercase().contains("win") ->
            "$userHome/AppData/Roaming/$appName"

        else -> // Linux or others
            "$userHome/.local/share/$appName"
    }

    // Ensure the directory exists
    File(directory).mkdirs()
    return directory
}
