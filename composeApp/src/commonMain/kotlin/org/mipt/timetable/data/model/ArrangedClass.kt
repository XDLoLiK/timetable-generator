package org.mipt.timetable.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ArrangedClass(
    val day: WeekDay,
    val group: String,
    val classNumber: Int,
    val teacher: String,
    val room: String,
)
