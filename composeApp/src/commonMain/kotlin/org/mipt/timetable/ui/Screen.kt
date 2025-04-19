package org.mipt.timetable.ui

sealed class Screen {
    object Home : Screen()
    object Input : Screen()
    object Export: Screen()
}