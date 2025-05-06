package org.mipt.timetable.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Group(
    val name: String = "",
    val classHours: Map<String, Int> = mapOf(),
)
