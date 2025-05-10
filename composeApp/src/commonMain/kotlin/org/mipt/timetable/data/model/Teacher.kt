package org.mipt.timetable.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Teacher(
    val name: String = "",
    val classes: Set<String> = setOf(),
    val groupWhitelist: Set<String> = setOf(),
    val timeslots: Map<WeekDay, List<Boolean>> = WeekDay.entries.associateWith {
        List(7) {
            false
        }
    },
)
