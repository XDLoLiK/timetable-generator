package org.mipt.timetable.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val name: String = "",
    val classHours: Map<String, Int> = mapOf(),
    val groupWhitelist: Set<String> = setOf("All"),
    val timeslots: Map<WeekDay, List<Boolean>> = WeekDay.entries.associateWith {
        List(8) {
            false
        }
    },
)
