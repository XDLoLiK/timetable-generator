package org.mipt.timetable.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class SolverStatus {
    SOLVING_SCHEDULED,
    SOLVING_ACTIVE,
    NOT_SOLVING
}
