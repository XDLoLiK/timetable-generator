package org.mipt.timetable.bloc.teacher

import org.mipt.timetable.data.model.Teacher
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class TeacherState(
    val teachers: Map<Uuid, Teacher> = mapOf()
)
