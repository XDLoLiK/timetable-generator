package org.mipt.timetable.bloc.teacher

import org.mipt.timetable.data.model.Teacher
import org.mipt.timetable.data.model.WeekDay
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
sealed class TeacherEvent {
    data class AddTeacher(val teacherId: Uuid, val teacher: Teacher) : TeacherEvent()
    data class RemoveTeacher(val teacherId: Uuid) : TeacherEvent()
    data class UpdateName(val teacherId: Uuid, val name: String) : TeacherEvent()
    data class AddClass(val teacherId: Uuid, val className: String) : TeacherEvent()
    data class RemoveClass(val teacherId: Uuid, val className: String) : TeacherEvent()
    data class AddGroupToWhitelist(val teacherId: Uuid, val group: String) : TeacherEvent()
    data class RemoveGroupFromWhitelist(val teacherId: Uuid, val group: String) : TeacherEvent()
    data class UpdateTimeslots(
        val roomId: Uuid,
        val timeslots: Map<WeekDay, List<Boolean>>
    ) : TeacherEvent()
    class ClearTeachers : TeacherEvent()
}
