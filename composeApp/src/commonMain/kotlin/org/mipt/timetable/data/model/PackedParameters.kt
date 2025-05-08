package org.mipt.timetable.data.model

import kotlinx.serialization.Serializable

@Serializable
data class PackedParameters(
    val rooms: List<Room>,
    val teachers: List<Teacher>,
    val groups: List<Group>,
)
