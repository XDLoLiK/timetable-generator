package org.mipt.timetable.bloc.teacher

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TeacherViewModel : ViewModel() {
    private val _state = MutableStateFlow(TeacherState())
    val state = _state.asStateFlow()

    fun onEvent(event: TeacherEvent) {
        when (event) {
            is TeacherEvent.AddTeacher -> onAddTeacher(event)
            is TeacherEvent.RemoveTeacher -> onRemoveTeacher(event)
            is TeacherEvent.UpdateName -> onUpdateName(event)
            is TeacherEvent.UpdateTimeslots -> onUpdateTimeslots(event)
            is TeacherEvent.AddClass -> onAddClass(event)
            is TeacherEvent.RemoveClass -> onRemoveClass(event)
            is TeacherEvent.AddGroupToWhitelist -> onAddGroupToWhitelist(event)
            is TeacherEvent.RemoveGroupFromWhitelist -> onRemoveGroupFromWhitelist(event)
        }
    }

    private fun onAddTeacher(event: TeacherEvent.AddTeacher) {
        _state.update {
            it.copy(
                teachers = it.teachers.toMutableMap().apply {
                    put(event.teacherId, event.teacher)
                }
            )
        }
    }

    private fun onRemoveTeacher(event: TeacherEvent.RemoveTeacher) {
        _state.update {
            it.copy(
                teachers = it.teachers.toMutableMap().apply {
                    remove(event.teacherId)
                }
            )
        }
    }

    private fun onUpdateName(event: TeacherEvent.UpdateName) {
        _state.update {
            it.copy(
                teachers = it.teachers.toMutableMap().apply {
                    val teacher = get(event.teacherId)
                    if (teacher != null) {
                        put(event.teacherId, teacher.copy(name = event.name))
                    }
                }
            )
        }
    }

    private fun onUpdateTimeslots(event: TeacherEvent.UpdateTimeslots) {
        _state.update {
            it.copy(
                teachers = it.teachers.toMutableMap().apply {
                    val room = get(event.roomId)
                    if (room != null) {
                        put(event.roomId, room.copy(timeslots = event.timeslots))
                    }
                }
            )
        }
    }

    private fun onAddClass(event: TeacherEvent.AddClass) {
        _state.update {
            it.copy(
                teachers = it.teachers.toMutableMap().apply {
                    val teacher = get(event.teacherId)
                    if (teacher != null) {
                        put(event.teacherId, teacher.copy(classHours = teacher.classHours.toMutableMap().apply {
                            put(event.classData.first, event.classData.second)
                        }))
                    }
                }
            )
        }
    }

    private fun onRemoveClass(event: TeacherEvent.RemoveClass) {
        _state.update {
            it.copy(
                teachers = it.teachers.toMutableMap().apply {
                    val teacher = get(event.teacherId)
                    if (teacher != null) {
                        put(event.teacherId, teacher.copy(classHours = teacher.classHours.toMutableMap().apply {
                            remove(event.className)
                        }))
                    }
                }
            )
        }
    }

    private fun onAddGroupToWhitelist(event: TeacherEvent.AddGroupToWhitelist) {
        _state.update {
            it.copy(
                teachers = it.teachers.toMutableMap().apply {
                    val teacher = get(event.teacherId)
                    if (teacher != null) {
                        put(event.teacherId, teacher.copy(groupWhitelist = teacher.groupWhitelist.toMutableSet().apply {
                            add(event.group)
                        }))
                    }
                }
            )
        }
    }

    private fun onRemoveGroupFromWhitelist(event: TeacherEvent.RemoveGroupFromWhitelist) {
        _state.update {
            it.copy(
                teachers = it.teachers.toMutableMap().apply {
                    val teacher = get(event.teacherId)
                    if (teacher != null) {
                        put(event.teacherId, teacher.copy(groupWhitelist = teacher.groupWhitelist.toMutableSet().apply {
                            remove(event.group)
                        }))
                    }
                }
            )
        }
    }
}
