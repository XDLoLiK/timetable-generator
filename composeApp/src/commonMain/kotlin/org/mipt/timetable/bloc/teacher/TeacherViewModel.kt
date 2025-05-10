package org.mipt.timetable.bloc.teacher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mipt.timetable.Database
import org.mipt.timetable.bloc.room.RoomState
import org.mipt.timetable.bloc.settings.SettingsState
import org.mipt.timetable.util.*
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
class TeacherViewModel(private val settingsFlow: StateFlow<SettingsState>)  : ViewModel() {
    private var _db: Database? = null
    private val _state = MutableStateFlow(TeacherState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsFlow.collect {
                if (it is SettingsState.Saved) {
                    _db = createDatabase(it.settings.dbDir)
                    _state.update { TeacherState(_db!!.getTeachers()) }
                }
            }
        }
    }

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
            is TeacherEvent.ClearTeachers -> onClearTeachers(event)
        }
        _db?.syncTeachers(_state.value.teachers)
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
                        put(event.teacherId, teacher.copy(classHours = teacher.classHours.toMutableSet().apply {
                            add(event.className)
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
                        put(event.teacherId, teacher.copy(classHours = teacher.classHours.toMutableSet().apply {
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

    private fun onClearTeachers(event: TeacherEvent.ClearTeachers) {
        _state.update { TeacherState() }
    }
}
