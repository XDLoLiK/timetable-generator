package org.mipt.timetable.data

import kotlinx.serialization.Serializable

@Serializable
data class TimetableData(
    var timetable: MutableList<ArrangedClassData> = mutableListOf(),
)
