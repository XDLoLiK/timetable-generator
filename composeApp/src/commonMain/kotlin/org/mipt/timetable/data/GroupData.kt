package org.mipt.timetable.data

data class GroupData(
    var name: String = "",
    var classHours: MutableMap<String, Int> = mutableMapOf(),
)
