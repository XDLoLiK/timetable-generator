package org.mipt.timetable.data

import kotlinx.serialization.Serializable

@Serializable
data class RoomData(
    var name: String = "",
    var timeslots: Map<WeekDay, BooleanArray> = WeekDay.values().associateWith { BooleanArray(8) { false } },
)
