package org.mipt.timetable.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val name: String = "",
    val timeslots: Map<WeekDay, List<Boolean>> = WeekDay.entries.associateWith {
        List(7) {
            false
        }
    },
)
