package org.mipt.timetable.data

import kotlinx.serialization.Serializable

@Serializable
data class TeacherData(
    var name: String = "",
    var timeslots: Map<WeekDay, BooleanArray> = WeekDay.values().associateWith { BooleanArray(8) { false } },
    var classHours: MutableMap<String, Int> = mutableMapOf(),
    var groupWhitelist: MutableSet<String> = mutableSetOf(),
)
