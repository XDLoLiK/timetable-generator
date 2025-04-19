package org.mipt.timetable.data

import kotlinx.serialization.Serializable

@Serializable
data class ArrangedClassData(
    val day: WeekDay,
    val group: String,
    val classNumber: Int,
    val teacher: String,
    val room: String,
)
